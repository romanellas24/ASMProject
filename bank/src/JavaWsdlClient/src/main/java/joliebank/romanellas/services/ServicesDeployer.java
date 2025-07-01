package joliebank.romanellas.services;

import io.camunda.zeebe.client.ZeebeClient;

import java.util.Map;

// Importa le classi generate da wsimport
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

import javax.xml.ws.Holder;

public class ServicesDeployer {

    public static void main(String[] args) {
        ZeebeClient client = ZeebeClient.newClientBuilder()
                .gatewayAddress("116.203.198.188:26500")  // Modifica se usi Camunda SaaS o altro endpoint
                .usePlaintext()
                .build();

        CreateTokenService createTokenService = new CreateTokenService(client);
        PayTokenService payTokenService = new PayTokenService(client);

        createTokenService.jolieCheckAccountExists();
        createTokenService.jolieCreateToken();
        payTokenService.jolieCheckToken();
        payTokenService.jolieCheckPayData();
        payTokenService.joliePayData();

        System.out.println("SOAP Workers avviati...");

        // Mantieni il worker in esecuzione
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }

        client.close();
    }
}
