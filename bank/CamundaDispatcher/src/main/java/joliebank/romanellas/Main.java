package joliebank.romanellas;

import com.example.soapclient.BANKGATEWAY2;
import com.example.soapclient.BANKGATEWAY2Service;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println(jakarta.xml.ws.spi.Provider.provider().getClass().getName());

        BANKGATEWAY2Service service = new BANKGATEWAY2Service();
        BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

        /*
        Map<String, Object> vars = job.getVariablesAsMap();
        double amount = (double) vars.get("amount");
        int account = (int) vars.get("account");
         */

        String response = port.postPay(24.08, 3);
        System.out.println("Created token: " + response);

        // Completa job
        /*
        jobClient.newCompleteCommand(job.getKey())
                .variables(Map.of("token", response))
                .send()
                .join();
         */
    }
}