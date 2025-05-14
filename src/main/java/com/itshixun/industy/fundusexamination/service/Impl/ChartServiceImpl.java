package com.itshixun.industy.fundusexamination.service.Impl;

import com.itshixun.industy.fundusexamination.domain.vo.ChartVO;
import com.itshixun.industy.fundusexamination.domain.vo.DailyCountVO;
import com.itshixun.industy.fundusexamination.repository.CaseRepository;
import com.itshixun.industy.fundusexamination.repository.PatientInfoRepository;
import com.itshixun.industy.fundusexamination.service.ChartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChartServiceImpl implements ChartService {
    @Autowired
    private PatientInfoRepository patientInfoRepository;
    @Autowired
    private CaseRepository caseRepository;
//    @AddCache(prefix = "chart",expire = 60*10)
    @Override
    public ChartVO selectAll() {

        ChartVO chartVO = new ChartVO();
        //1.今日新增
        chartVO.setTotalPatientData(countTodayPatients());
        //2.获取本周每天的数据
        // 2.本周每日数据（新增部分）
        // 修改后的日期范围计算方式
        LocalDateTime weekStart = LocalDate.now()
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .atStartOfDay();
        List<DailyCountVO> rawData = getWeeklyPatients(weekStart);
        chartVO.setWeekPatientData(rawData);
        // 3.待诊断
        // 今日统计
        chartVO.setTodayFinishedPatientData(countByStatusToday(1));  // 状态1的数量
        //4.已经诊断
        chartVO.setTodayReadyPatientData(countByStatusToday(2));
        //5.年龄分布
        Map<String, Integer> DageData = getAgeDistributionByDisease("糖尿病");
        Map<String, Integer> CageData = getAgeDistributionByDisease("白内障");
        Map<String, Integer> AageData = getAgeDistributionByDisease("AMD");
        Map<String, Integer> GAgeData = getAgeDistributionByDisease("青光眼");
        Map<String, Integer> HAgeData = getAgeDistributionByDisease("高血压");
        Map<String, Integer> MAgeData = getAgeDistributionByDisease("近视");
        Map<String, Integer> OAgeData = getAgeDistributionByDisease("其他");
        chartVO.setDAgeData((LinkedHashMap<String, Integer>) DageData);
        chartVO.setCAgeData((LinkedHashMap<String, Integer>) CageData);
        chartVO.setAAgeData((LinkedHashMap<String, Integer>) AageData);
        chartVO.setGAgeData((LinkedHashMap<String, Integer>) GAgeData);
        chartVO.setHAgeData((LinkedHashMap<String, Integer>) HAgeData);
        chartVO.setMAgeData((LinkedHashMap<String, Integer>) MAgeData);
        chartVO.setOAgeData((LinkedHashMap<String, Integer>) OAgeData);


        //6.性别分布
        Map<String, Integer> DGenderData = getGenderDistributionByDisease("糖尿病");
        Map<String, Integer> CGenderData = getGenderDistributionByDisease("白内障");
        Map<String, Integer> AGenderData = getGenderDistributionByDisease("AMD");
        Map<String, Integer> GGenderData = getGenderDistributionByDisease("青光眼");
        Map<String, Integer> HGenderData = getGenderDistributionByDisease("高血压");
        Map<String, Integer> MGenderData = getGenderDistributionByDisease("近视");
        Map<String, Integer> OGenderData = getGenderDistributionByDisease("其他");
        chartVO.setDGenderData((LinkedHashMap<String, Integer>) DGenderData);
        chartVO.setCGenderData((LinkedHashMap<String, Integer>) CGenderData);
        chartVO.setGGenderData((LinkedHashMap<String, Integer>) GGenderData);
        chartVO.setHGenderData((LinkedHashMap<String, Integer>) HGenderData);
        chartVO.setMGenderData((LinkedHashMap<String, Integer>) MGenderData);
        chartVO.setOGenderData((LinkedHashMap<String, Integer>) OGenderData);
        chartVO.setAGenderData((LinkedHashMap<String, Integer>) AGenderData);


        return chartVO;
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

    public List<DailyCountVO> getWeeklyPatients(LocalDateTime weekStart) {
        List<DailyCountVO> result = new ArrayList<>(7);

        for (int i = 0; i < 7; i++) {
            LocalDateTime dayStart = weekStart.plusDays(i).with(LocalTime.MIN);
            LocalDateTime dayEnd = dayStart.plusDays(1);

            Integer count = patientInfoRepository.countByCreateDateBetween(dayStart, dayEnd);
            result.add(new DailyCountVO(i + 1, count)); // 根据实际星期值调整
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
