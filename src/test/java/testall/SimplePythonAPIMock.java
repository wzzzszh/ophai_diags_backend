package testall;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/simple-python-api")
public class SimplePythonAPIMock {

    // 最简版POST接口 - 直接返回输入参数的output
    @PostMapping("/algorithm")
    public ResponseEntity<String> simpleResponse(@RequestBody Map<String, String> request) {
        String input = request.get("input");
        return ResponseEntity.ok("模拟Python输出: " + input);
    }
}