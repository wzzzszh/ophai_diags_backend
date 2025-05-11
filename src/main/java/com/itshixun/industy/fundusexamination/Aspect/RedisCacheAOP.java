package com.itshixun.industy.fundusexamination.Aspect;

import com.itshixun.industy.fundusexamination.Interface.AddCache;
import com.itshixun.industy.fundusexamination.Interface.DelCache;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.logging.Logger;

@Component
@Aspect
@RequiredArgsConstructor
public class RedisCacheAOP {

    // 手动初始化 Logger
    private static final Logger logger = Logger.getLogger(RedisCacheAOP.class.getName());

    private final RedisTemplate<String, Object> redisTemplate;


    @Pointcut("@annotation(com.itshixun.industy.fundusexamination.Interface.AddCache)")
    public void addCache() {

    }
    @Pointcut("@annotation(com.itshixun.industy.fundusexamination.Interface.DelCache)")
    public void delCache() {

    }
    @Around("addCache()")
    public Object addCache(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取注解
        AddCache addCache = method.getAnnotation(AddCache.class);
        //获取前缀
        String prefix = addCache.prefix();
        //获取过期时间
        long expire = addCache.expire();
        //生成缓存的key
        String key = prefix + "::"+joinPoint.getArgs()[0];
        try {
            //从缓存中获取数据
            Object cacheValue = redisTemplate.opsForValue().get(key);
            // 如果缓存中有数据，直接返回
            if (cacheValue != null) {

                logger.info("从缓存中获取数据: " + key);
                return cacheValue;
            }
        }catch (Exception e) {
            logger.severe("缓存获取失败: " + key);
        }
        // 执行方法
        logger.info("执行方法: " + key);
        Object result = joinPoint.proceed();

        // 如果结果不为空，则存入缓存
        if (result != null) {
            try {
                if (expire > 0) {
                    redisTemplate.opsForValue().set(key, result, expire, java.util.concurrent.TimeUnit.SECONDS);
                } else {
                    redisTemplate.opsForValue().set(key, result);
                }
                logger.info("缓存写入成功: " + key);
            } catch (Exception e) {
                logger.severe("缓存写入失败: " + key);
                logger.severe(e.getMessage());
                // 缓存写入异常不影响返回结果
            }
        }
        return result;
    }
    @AfterReturning("delCache()")
    public void delCache(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取注解
        DelCache delCache = method.getAnnotation(DelCache.class);
        //获取前缀
        String prefix = delCache.prefix();
        //获取key参数索引
        int keyIndex = delCache.keyIndex();
        Object[] args = joinPoint.getArgs();
        //执行
        if(args.length>keyIndex){

            // 1. 如果知道确切的键，直接删除指定键
            String key = prefix + "::"+args[keyIndex];
            redisTemplate.delete(key);
            logger.info("缓存删除成功: " + key);
//            // 2. 或者使用模式匹配，删除所有相关缓存（更安全但性能较低）
//            String pattern = prefix + "::*";
//            Set<String> keys = redisTemplate.keys(pattern);
//            if (keys != null && !keys.isEmpty()) {
//                redisTemplate.delete(keys);
//                logger.info("删除缓存前缀 {} 下的所有数据, 共 {} 条");
//            }

        }
    }
}
