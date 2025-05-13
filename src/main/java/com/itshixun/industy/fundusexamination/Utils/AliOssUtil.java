package com.itshixun.industy.fundusexamination.Utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
@Slf4j
@Component
public class AliOssUtil {


    private final Dotenv dotenv = Dotenv.load();

    private final String ENDPOINT = dotenv.get("ALI_OSS_ENDPOINT");
    private final String ACCESS_KEY_ID = dotenv.get("ALI_OSS_ACCESS_KEY_ID");
    private final String SECRET_ACCESS_KEY = dotenv.get("ALI_OSS_SECRET_ACCESS_KEY");
    private static final Logger logger = LoggerFactory.getLogger(AliOssUtil.class);

    private static final String BUCKET_NAME = "firstgogogo";
    // 域名
    private static final String ENDPOINT_ALI = "image.fivecoco.xyz";



    //上传文件,返回文件的公网访问地址
    public String uploadFile(String objectName, InputStream inputStream){

        OSS ossClient = new OSSClientBuilder().build(
                ENDPOINT,
                ACCESS_KEY_ID,
                SECRET_ACCESS_KEY
        );
        //公文访问地址
        //文件夹名称+文件名
        String prefix = objectName.split("_")[0]+"_"+objectName.split("_")[1];
        objectName = prefix+"/"+objectName;
        String url = "";
        try {
            // 创建存储空间。
            ossClient.createBucket(BUCKET_NAME);
            ossClient.putObject(BUCKET_NAME, objectName, inputStream);
//            url = "https://"+BUCKET_NAME+"."+ENDPOINT.substring(ENDPOINT.lastIndexOf("/")+1)+"/"+objectName;
            url = "https://"+ENDPOINT_ALI+"/"+objectName;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return url;
    }

    public InputStream downloadFile(String leftImage) {
        leftImage =leftImage.split("/")[3]+"/"+leftImage.split("/")[4];
//        log.info("这是图像相对路径"+leftImage);
        OSS ossClient = new OSSClientBuilder().build(
                ENDPOINT,
                ACCESS_KEY_ID,
                SECRET_ACCESS_KEY
        );
        InputStream inputStream = null;
        try {
            // 下载文件到本地。
            inputStream = ossClient.getObject(BUCKET_NAME, leftImage).getObjectContent();
            byte[] bytes = toByteArray(inputStream);

            return new ByteArrayInputStream(bytes);
        } catch (OSSException oe) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.", oe);
            throw new OSSException("OSS Exception", oe);
        } catch (IOException e) {
            log.error("Caught an IOException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.", e);
            throw new OSSException("OSS Exception", e);
        }
    }
    /**
     * InputStream流转byte数组
     *
     * @param input
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[input.available()];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

}
