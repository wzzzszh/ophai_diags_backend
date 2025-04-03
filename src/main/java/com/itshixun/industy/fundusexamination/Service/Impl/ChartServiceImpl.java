package com.itshixun.industy.fundusexamination.Service.Impl;

import com.itshixun.industy.fundusexamination.Service.chartService;
import com.itshixun.industy.fundusexamination.pojo.dto.DailyCountDTO;
import com.itshixun.industy.fundusexamination.pojo.dto.chartDto;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class ChartServiceImpl implements chartService {
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Override
    public chartDto selectAll() {

        chartDto chartDto = new chartDto();
        //1.今日新增
        chartDto.setTotalPatientData(countTodayPatients());
        //2.获取本周每天的数据
        // 2.本周每日数据（新增部分）
        // 修改后的日期范围计算方式
        LocalDateTime weekStart = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        List<DailyCountDTO> rawData = getWeeklyPatients(weekStart);
//        System.out.println(rawData.toString());
        chartDto.setWeekPatientData(rawData);
        // 3.待诊断
        // 今日统计
        chartDto.setTodayFinishedPatientData(countByStatusToday(1));  // 状态1的数量
        //4.已经诊断
        chartDto.setTodayReadyPatientData(countByStatusToday(2));
        //5.年龄分布
        Map<String, Integer> DageData = getAgeDistributionByDisease("糖尿病");
        Map<String, Integer> CageData = getAgeDistributionByDisease("白内障");
        Map<String, Integer> AageData = getAgeDistributionByDisease("AMD");
        Map<String, Integer> GAgeData = getAgeDistributionByDisease("青光眼");
        Map<String, Integer> HAgeData = getAgeDistributionByDisease("高血压");
        Map<String, Integer> MAgeData = getAgeDistributionByDisease("近视");
        Map<String, Integer> OAgeData = getAgeDistributionByDisease("其他");
        chartDto.setDAgeData((LinkedHashMap<String, Integer>) DageData);
        chartDto.setCAgeData((LinkedHashMap<String, Integer>) CageData);
        chartDto.setAAgeData((LinkedHashMap<String, Integer>) AageData);
        chartDto.setGAgeData((LinkedHashMap<String, Integer>) GAgeData);
        chartDto.setHAgeData((LinkedHashMap<String, Integer>) HAgeData);
        chartDto.setMAgeData((LinkedHashMap<String, Integer>) MAgeData);
        chartDto.setOAgeData((LinkedHashMap<String, Integer>) OAgeData);





//        Map<String, Integer> DageData = getAgeDistributionByDisease("神经病");
//        chartDto.setDAgeData((LinkedHashMap<String, Integer>) DageData);
        //6.性别分布
        Map<String, Integer> DGenderData = getGenderDistributionByDisease("糖尿病");
        Map<String, Integer> CGenderData = getGenderDistributionByDisease("白内障");
        Map<String, Integer> AGenderData = getGenderDistributionByDisease("AMD");
        Map<String, Integer> GGenderData = getGenderDistributionByDisease("青光眼");
        Map<String, Integer> HGenderData = getGenderDistributionByDisease("高血压");
        Map<String, Integer> MGenderData = getGenderDistributionByDisease("近视");
        Map<String, Integer> OGenderData = getGenderDistributionByDisease("其他");
        chartDto.setDGenderData((LinkedHashMap<String, Integer>) DGenderData);
        chartDto.setCGenderData((LinkedHashMap<String, Integer>) CGenderData);
        chartDto.setGGenderData((LinkedHashMap<String, Integer>) GGenderData);
        chartDto.setHGenderData((LinkedHashMap<String, Integer>) HGenderData);
        chartDto.setMGenderData((LinkedHashMap<String, Integer>) MGenderData);
        chartDto.setOGenderData((LinkedHashMap<String, Integer>) OGenderData);
        chartDto.setAGenderData((LinkedHashMap<String, Integer>) AGenderData);
//        genderData.put("男", patientInfoRepository.countByGender(1));

        return chartDto;
    }


    //今日新增
    public Integer countTodayPatients() {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrowStart = todayStart.plusDays(1);
        return patientInfoRepository.countByCreateDateBetween(todayStart, tomorrowStart);
    }

    // 新增统计方法
    private Integer countByStatusToday(Integer status) {
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrowStart = todayStart.plusDays(1);

        return caseRepository.countByStatusAndDate(
                status, todayStart, tomorrowStart
        );
    }

    public List<DailyCountDTO> getWeeklyPatients(LocalDateTime weekStart) {
        List<DailyCountDTO> result = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = weekStart.plusDays(i).with(LocalTime.MIN);
            LocalDateTime dayEnd = dayStart.plusDays(1);

            Integer count = patientInfoRepository.countByCreateDateBetween(dayStart, dayEnd);
            result.add(new DailyCountDTO(i + 1, count)); // 根据实际星期值调整
        }
        return result;
    }
    public Map<String, Integer> getAgeDistributionByDisease(String diseaseName) {
        // 1. 查询包含该疾病名称的所有患者ID
        List<String> patientIds = caseRepository.findPatientIdsByDiseaseNameContaining(diseaseName);

        // 2. 按年龄段统计
        Map<String, Integer> ageDistribution = new LinkedHashMap<>();

        ageDistribution.put("0-17", countPatientsInAgeGroup(patientIds, 0, 17));
        ageDistribution.put("18-34", countPatientsInAgeGroup(patientIds, 18, 34));
        ageDistribution.put("35-49", countPatientsInAgeGroup(patientIds, 35, 49));
        ageDistribution.put("50-64", countPatientsInAgeGroup(patientIds, 50, 64));
        ageDistribution.put("65+", countPatientsInAgeGroup(patientIds, 65, 200));

        return ageDistribution;
    }

    private int countPatientsInAgeGroup(List<String> patientIds, int startAge, int endAge) {
        if (patientIds.isEmpty()) {
            return 0;
        }
        return patientInfoRepository.countByAgeRangeAndPatientIds(patientIds, startAge, endAge);
    }

    // ... 其他方法 ...

    public Map<String, Integer> getGenderDistributionByDisease(String diseaseName) {
        // 1. 查询包含该疾病名称的所有患者ID
        List<String> patientIds = caseRepository.findPatientIdsByDiseaseNameContaining(diseaseName);

        // 2. 按性别统计
        Map<String, Integer> genderDistribution = new LinkedHashMap<>();
        genderDistribution.put("男", countPatientsByGender(patientIds, 0));
        genderDistribution.put("女", countPatientsByGender(patientIds, 1));

        return genderDistribution;
    }

    private int countPatientsByGender(List<String> patientIds, int genderCode) {
        if (patientIds.isEmpty()) {
            return 0;
        }
        return patientInfoRepository.countByGenderAndPatientIds(patientIds, genderCode);
    }


}
