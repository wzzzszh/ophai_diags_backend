package testall;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testall.AlgorithmRequest;
import testall.AlgorithmResponse;
import testall.AlgorithmService;

@RestController
@RequestMapping("/api")
public class AlgorithmController {
    @Autowired
    private AlgorithmService algorithmService;

    @PostMapping("/execute-algorithm")
    public ResponseEntity<AlgorithmResponse> executeAlgorithm(
            @Valid @RequestBody AlgorithmRequest request
    ) {
        AlgorithmResponse response = algorithmService.callAlgorithm(request);
        return ResponseEntity.ok(response);
    }
}