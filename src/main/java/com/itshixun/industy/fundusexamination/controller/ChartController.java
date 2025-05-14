package com.itshixun.industy.fundusexamination.controller;

import com.itshixun.industy.fundusexamination.service.ChartService;
import com.itshixun.industy.fundusexamination.utils.ResponseMessage;
import com.itshixun.industy.fundusexamination.domain.vo.ChartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chart")
public class ChartController {
    @Autowired
    private ChartService chartService;
    @PostMapping
    public ResponseMessage<ChartVO> selectChart() {
        ChartVO chartVO = chartService.selectAll();
        return ResponseMessage.success(chartVO);
    }
}
