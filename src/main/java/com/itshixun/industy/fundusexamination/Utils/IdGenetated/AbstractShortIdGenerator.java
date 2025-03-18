//package com.itshixun.industy.fundusexamination.Utils.IdGenetated;
//
//import org.springframework.stereotype.Component;
//import java.util.Random;
//import java.util.UUID;
//
//@Component
//public abstract class AbstractShortIdGenerator {
//
//    // 核心配置参数
//    private final long workerId;       // 3位（0-7，用于分布式标识）
//    private final int businessCode;    // 2位（0-3，业务类型编码）
//    private static final Random random = new Random();
//    private volatile long lastTimestamp = -1L;
//
//    // 构造函数注入参数（建议通过配置管理）
//    public AbstractShortIdGenerator(long workerId, int businessCode) {
//        this.workerId = workerId;
//        this.businessCode = businessCode;
//    }
//
//    /**
//     * 生成16位短ID（示例：USER_6E2F3A9B）
//     */
//    public static final String generateShortId() {
//        long timestamp = System.currentTimeMillis();
//        String timestampPart = encodeTimestamp(timestamp);  // 6位Base62编码
//        String randomPart = encodeRandom(6);               // 6位随机Base62
//        String checksum = calculateChecksum(timestampPart + randomPart); // 2位校验
//        return String.format("%s_%s%s%s",
//                getBusinessPrefix(),
//                timestampPart,
//                randomPart.substring(0, 4), // 节流随机数以保证总长度
//                checksum
//        );
//    }
//
//
//
//    // 时间戳编码（Base62，6位，覆盖约3年）
//    private static String encodeTimestamp(long timestamp) {
//        long epoch = timestamp - 1609459200000L; // 2021-01-01起始
//        long code = epoch % (62 * 62 * 62 * 62 * 62 * 62); // 6位Base62
//        return base62Encode(code);
//    }
//
//    // 随机数编码（Base62，6位）
//    private static String encodeRandom(int length) {
//        long randomCode = random.nextLong() & ((1L << length) - 1);
//        return base62Encode(randomCode);
//    }
//
//    // CRC校验（2位）
//    private static String calculateChecksum(String data) {
//        int crc = 0xFFFF;
//        for (char c : data.toCharArray()) {
//            crc = ((crc >> 8) ^ c) & 0xFF;
//            crc = (crc ^ (crc >> 4)) & 0xFF;
//            crc = (crc ^ (crc >> 2)) & 0xFF;
//            crc = (crc ^ (crc >> 1)) & 0xFF;
//        }
//        return String.format("%02X", crc & 0xFF);
//    }
//
//    // Base62编码工具
//    private static String base62Encode(long num) {
//        StringBuilder sb = new StringBuilder();
//        String base62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
//        while (num > 0) {
//            sb.append(base62.charAt((int) (num % 62)));
//            num /= 62;
//        }
//        return sb.reverse().toString();
//    }
//
//    protected static String getBusinessPrefix() {
//        return null;
//    }
//}