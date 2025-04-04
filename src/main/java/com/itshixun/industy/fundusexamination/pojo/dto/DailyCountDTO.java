package com.itshixun.industy.fundusexamination.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
// 必须确保DTO类有以下精确匹配的构造函数
public class DailyCountDTO {
    private final Integer dayOfWeek;  // 参数名称需要与JPQL中DAYOFWEEK结果对应
    private final Integer count;        // 必须使用Long类型接收COUNT结果

}