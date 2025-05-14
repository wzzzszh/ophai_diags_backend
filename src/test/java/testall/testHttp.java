package testall;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itshixun.industy.fundusexamination.domain.dto.AICaseInfoDTO;

public class testHttp {
    // 初始化 ObjectMapper
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public static void main(String[] args) throws JsonProcessingException {


        String url = " {\"predictions\": {\"D\": 0.9981995820999146, \"G\": 0.0004687512409873307, \"C\": 5.035019785282202e-05, \"A\": 8.038042869884521e-05, \"H\": 0.92125004529953, \"M\": 3.438738531258423e-06, \"O\": 0.13620536029338837}, \"images\": {\"heatmaps\": {\"contains\": [\"D\", \"H\"], \"left\": {\"D\": [\"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_D_block1_conv1_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_D_block3_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_D_block4_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_D_block8_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_D_block12_sepconv3_act_heatmap.jpg\"], \"H\": [\"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_H_block1_conv1_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_H_block3_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_H_block4_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_H_block8_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_H_block12_sepconv3_act_heatmap.jpg\"]}, \"right\": {\"D\": [\"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_D_block1_conv1_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_D_block3_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_D_block4_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_D_block8_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_D_block12_sepconv3_act_heatmap.jpg\"], \"H\": [\"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_H_block1_conv1_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_H_block3_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_H_block4_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_H_block8_sepconv2_act_heatmap.jpg\", \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_H_block12_sepconv3_act_heatmap.jpg\"]}}, \"vessels\": {\"left\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_vessel.jpg\", \"right\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_vessel.jpg\"}, \"disks\": {\"left\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/left_disk.jpg\", \"right\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/right_disk.jpg\"}, \"original\": {\"left\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/eyeTest/preprocessed_images/11_left.jpg\", \"right\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/eyeTest/preprocessed_images/11_right.jpg\"}}, \"suggestions\": [\"视网膜血管呈现异常变化，可能与全身性疾病相关\", \"黄斑区颜色略显暗淡，需关注其功能状态\", \"视盘区域边界清晰，但周围血管有轻微迂曲现象\"], \"revisit_time\": \"2025-05-04\", \"drugs\": [{\"function\": \"降糖\", \"drug\": [\"二甲双胍\", \"阿卡波糖\", \"格列齐特\"]}, {\"function\": \"降压\", \"drug\": [\"氨氯地平\", \"缬沙坦\", \"美托洛尔\"]}, {\"function\": \"改善视网膜\", \"drug\": [\"雷珠单抗\", \"康柏西普\", \"贝伐单抗\"]}], \"report_html\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/report.html\", \"qr_code\": \"https://firstgogogo.oss-cn-beijing.aliyuncs.com/11/qr_code.jpg\"}";
        System.out.println(url);
        //去除url字段中所有的"\",
        url = url.replace("\\", "");
        System.out.println(url);
        AICaseInfoDTO aiCaseInfo = objectMapper.readValue(url, AICaseInfoDTO.class);
        System.out.println(aiCaseInfo);

    }

}

