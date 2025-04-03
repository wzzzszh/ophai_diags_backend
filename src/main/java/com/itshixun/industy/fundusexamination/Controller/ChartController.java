package com.itshixun.industy.fundusexamination.Controller;

import com.itshixun.industy.fundusexamination.Service.chartService;
import com.itshixun.industy.fundusexamination.Utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.pojo.dto.chartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chart")
public class ChartController {
    @Autowired
    private chartService chartService;
    @PostMapping
    public ResponseMessage<chartDto> selectChart() {

        chartDto chartDto = chartService.selectAll();


        return ResponseMessage.success(chartDto);


    }
}
