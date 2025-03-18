package com.itshixun.industy.fundusexamination.Utils.IdGenetated;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class TableNameIdGenerator implements IdentifierGenerator {

    // 线程安全序列号生成器（替代 ThreadLocal）
    private static final AtomicLong SEQUENCE = new AtomicLong(0);

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // 获取实体类名（默认表名）
        String entityName = object.getClass().getSimpleName().toLowerCase();

        // 构建 ID 组件
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(11); // 最后 10 位时间戳
        long seq = SEQUENCE.incrementAndGet() % 9999; // 4 位序列号（0000-9999）

        // 组合并校验长度（总长 = 5(prefix) + 10(timestamp) + 4(seq) = 19）
        String id = String.format("%s_%s%04d", entityName, timestamp, seq);

        return id;
    }
}