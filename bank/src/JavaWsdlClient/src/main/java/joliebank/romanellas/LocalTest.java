package joliebank.romanellas;

import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2;
import wsdl.cloud.romanellas.joliebank.BANKGATEWAY2Service;

public class LocalTest {
    public static void main(String[] args) {
        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        try {
            BANKGATEWAY2Service service = new BANKGATEWAY2Service();
            BANKGATEWAY2 port = service.getBANKGATEWAY2ServicePort();

            // Invia la richiesta e ricevi la risposta
            String response = port.postPay(3.41, 1);

            // Stampa il risultato
            System.out.println("Risultato: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

