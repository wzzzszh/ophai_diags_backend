package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@Data
public class chartDto {


    Integer TodayFinishedPatientData;

    Integer TodayReadyPatientData;

    Integer TotalPatientData;

    //糖尿病
    LinkedHashMap<String, Integer> DAgeData;
    //白内障
    LinkedHashMap<String, Integer> CAgeData;
    //AMD
    LinkedHashMap<String, Integer> AAgeData;
    //青光眼
    LinkedHashMap<String, Integer> GAgeData;
    //高血压
    LinkedHashMap<String, Integer> HAgeData;
    //近视
    LinkedHashMap<String, Integer> MAgeData;
    //其他
    LinkedHashMap<String, Integer> OAgeData;


    //糖尿病
    LinkedHashMap<String, Integer> DGenderData;
    //白内障
    LinkedHashMap<String, Integer> CGenderData;
    //AMD
    LinkedHashMap<String, Integer> AGenderData;
    //青光眼
    LinkedHashMap<String, Integer> GGenderData;
    //高血压
    LinkedHashMap<String, Integer> HGenderData;
    //近视
    LinkedHashMap<String, Integer> MGenderData;
    //其他
    LinkedHashMap<String, Integer> OGenderData;

    // 新增周统计字段
    private List<DailyCountDTO> weekPatientData;

    // 内部类用于存储每日数据
    @Data
    public static class DailyPatientData {
        private LocalDate date;
        private Integer count;
    }
}
