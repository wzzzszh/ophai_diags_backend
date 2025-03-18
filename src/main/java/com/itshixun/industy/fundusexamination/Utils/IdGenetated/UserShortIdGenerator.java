//package com.itshixun.industy.fundusexamination.Utils.IdGenetated;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserShortIdGenerator extends AbstractShortIdGenerator {
//
//    public UserShortIdGenerator(
//            @Value("${snowflake.user.worker-id}") long workerId,
//            @Value("${snowflake.user.business-code}") int businessCode) {
//        super(workerId, businessCode);
//    }
//
//    @Override
//    protected String getBusinessPrefix() {
//        return "USER"; // 业务前缀
//    }
//}