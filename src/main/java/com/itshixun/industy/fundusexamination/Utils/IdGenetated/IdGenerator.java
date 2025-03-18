package com.itshixun.industy.fundusexamination.Utils.IdGenetated;

import com.itshixun.industy.fundusexamination.Utils.IdGenetated.PrefixType;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {

    // 线程局部存储序列号生成器
    private static final ThreadLocal<AtomicInteger> sequenceHolder =
            ThreadLocal.withInitial(AtomicInteger::new);

    /**
     * 生成符合规范的唯一ID
     * @param prefixType 前缀类型
     * @return 格式为"PREFIX+TIMESTAMP+SEQUENCE"的字符串
     */
    public static String generateId(PrefixType prefixType) {
        int availableLength = 16 - prefixType.getLength();
        if (availableLength <= 0) {
            throw new IllegalArgumentException("前缀长度不能超过" + (16 - 0) + "位");
        }

        // 获取时间戳组件（10位十六进制）
        long timestamp = System.currentTimeMillis();
        String timestampHex = Long.toHexString(timestamp).toUpperCase()
                .substring(64 - availableLength); // 取后N位

        // 生成序列号组件（动态长度）
        int sequenceLength = Math.max(1, availableLength - timestampHex.length());
        int sequence = sequenceHolder.get().getAndIncrement() % (int)Math.pow(10, sequenceLength);
        String sequenceStr = String.format("%0" + sequenceLength + "d", sequence);

        // 组合各部分并校验长度
        String id = prefixType.getPrefix() + timestampHex + sequenceStr;
        return id.substring(0, prefixType.getTotalLength());
    }
}