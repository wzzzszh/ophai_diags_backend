//package com.itshixun.industy.fundusexamination.Utils.IdGenetated;
//
//import com.itshixun.industy.fundusexamination.Utils.IdGenetated.AbstractShortIdGenerator;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class PatientShortIdGenerator extends AbstractShortIdGenerator {
//
//
//    public PatientShortIdGenerator(
//            @Value("${snowflake.patient.worker-id}") long workerId,
//            @Value("${snowflake.patient.business-code}") int businessCode) {
//        super(workerId, businessCode);
//    }
//
//    @Override
//    protected String getBusinessPrefix() {
//        return "PATIENT"; // 业务前缀
//    }
//}