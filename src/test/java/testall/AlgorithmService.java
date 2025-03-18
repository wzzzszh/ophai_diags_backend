package testall;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;  // 添加缺失的import
import org.springframework.http.MediaType;

@Service
public class AlgorithmService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String PYTHON_API_URL = "http://localhost:8088/api/algorithm"; // Python服务地址

    public AlgorithmResponse callAlgorithm(AlgorithmRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AlgorithmRequest> entity = new HttpEntity<>(request, headers);

        return restTemplate.postForObject(PYTHON_API_URL, entity, AlgorithmResponse.class);
    }
}