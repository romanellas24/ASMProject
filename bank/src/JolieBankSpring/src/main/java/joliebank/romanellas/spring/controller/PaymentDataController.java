package joliebank.romanellas.spring.controller;
import io.camunda.zeebe.client.ZeebeClient;
import joliebank.romanellas.spring.dto.PaymentDataRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentDataController {

    @Autowired
    private ZeebeClient zeebeClient;

    @PostMapping("/send-payment-data")
    public String sendPaymentData(@RequestBody PaymentDataRequestDto request) {
        try {
            zeebeClient.newPublishMessageCommand()
                    .messageName("JolieInsertPaymentData")
                    .correlationKey(request.getToken())
                    .variables(request.getData())
                    .send()
                    .join();

            return "Message sent to Camunda for token: " + request.getToken();
        } catch (Exception e) {
            return "Failed to send message: " + e.getMessage();
        }
    }
}

