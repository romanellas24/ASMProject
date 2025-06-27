package joliebank.romanellas;

import org.w3._2001.xmlschema.BANKGATEWAY;
import org.w3._2001.xmlschema.BANKGATEWAYService;

public class LocalTest {
    public static void main(String[] args) {
        try {
            // Inizializza il servizio (nome e metodo dipendono da quello che ha generato wsimport!)
            BANKGATEWAYService service = new BANKGATEWAYService();
            BANKGATEWAY port = service.getBANKGATEWAYServicePort();

            // Invia la richiesta e ricevi la risposta
            String response = port.postPay(3.41, 2);

            // Stampa il risultato
            System.out.println("Risultato: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

