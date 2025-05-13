package com.itshixun.industy.fundusexamination.Utils.RabbitMQ;

import com.itshixun.industy.fundusexamination.Service.CaseService;
import com.itshixun.industy.fundusexamination.Service.PreImageService;
import com.itshixun.industy.fundusexamination.pojo.Case;
import com.itshixun.industy.fundusexamination.pojo.dto.CaseDto;
import com.itshixun.industy.fundusexamination.pojo.httpEnity.ResponseData;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@RabbitListener(queues = RabbitMQConfig.IMAGE_PROCESS_QUEUE)
public class ImageProcessConsumer {
    // 添加消息转换器配置
    @Autowired
    private Jackson2JsonMessageConverter jsonMessageConverter;
    @Autowired
    private PreImageService preImageService;
    @Autowired
    private CaseService caseService;
    // 手动创建 Logger 实例
    private static final Logger logger = Logger.getLogger(ImageProcessConsumer.class.getName());
    @RabbitHandler
    public void process(@Payload ImageProcessMessage message) {
        try {
            ResponseData responseData = preImageService.sendUrltoP(

                    message.getPatientAge(),
                    message.getPatientGender(),
                    message.getPatientName(),
                    message.getCaseId(),
                    message.getLeftImageUrl(),
                    message.getRightImageUrl()
            );

            if (!responseData.getSuccess()) {
                // 处理失败逻辑（重试/记录日志等）
                throw new RuntimeException("病例ID为" +message.getCaseId()+ "的病例AI诊断失败");
            }
            // 更新数据库状态
            Case managedCase = caseService.getCaseById(message.getCaseId()); // 重新获取托管状态的实体
            managedCase.setAiCaseInfo(responseData.getMessage());
            CaseDto caseDto = new CaseDto();
            BeanUtils.copyProperties(managedCase, caseDto);
            caseDto.setDiagStatus(1);
            caseService.update(caseDto);
            logger.info("病例ID为" +message.getCaseId()+ "的病例AI诊断成功");

        } catch (Exception e) {
            // 增强错误处理
            logger.severe("消息处理失败: " + e.getMessage());
            // 记录完整错误日志
            e.printStackTrace();
            // TODO:添加重试逻辑或死信队列处理
        }
    }
}