package com.itshixun.industy.fundusexamination.Utils.interceptor;

import com.itshixun.industy.fundusexamination.Utils.JwtUtil;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.Utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //令牌验证
        String token = request.getHeader("Authorization");
        //
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            //把业务数据存到里面
            ThreadLocalUtil.set(claims);
            //放行
            return true;
        }catch (Exception e){
//            response.setStatus(401);
            ResponseMessage.loginerror();
            //不放行
            return false;
        }

    }
}
