package asm.couriers.courier_allocation.dto;


import asm.couriers.courier_allocation.exception.MapsServiceException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/*
{
  "destination_addresses" : [ "Campobasso, 86100 Campobasso, Province of Campobasso, Italy" ],
  "origin_addresses" : [ "Via Roma, 156-192, 86012 Cercemaggiore CB, Italy" ],
  "rows" : [ {
    "elements" : [ {
      "distance" : {
        "text" : "17.3 km",
        "value" : 17315
      },
      "duration" : {
        "text" : "24 mins",
        "value" : 1486
      },
      "origin" : "41.461300,14.724020",
      "destination" : "41.559470,14.667370",
      "status" : "OK"
    } ]
  } ],
  "status" : "OK"
}
 */
@Setter
@Getter
@ToString
@NoArgsConstructor
public class DistanceMatrixResponseDTO {

    @Setter
    @Getter
    @ToString
    public static class InfoDTO{
        private String text;
        private Integer value;
    }

    @Setter
    @Getter
    @ToString
    public static class ElementDTO {
        private InfoDTO distance;
        private InfoDTO duration;
        private String origin;
        private String destination;
        private String status;
    }

    @Setter
    @Getter
    @ToString
    public static class ElementsDTO {
       private ElementDTO[] elements;
    }

    private String[] destination_addresses;
    private String[] origin_addresses;
    private String status;
    private ElementsDTO[] rows;

    private void checkValidity() throws MapsServiceException {
        if (this.getRows() == null || this.getRows().length == 0) {
            throw new MapsServiceException("No route found");
        }
        if (this.getRows()[0].elements == null || this.getRows()[0].elements.length == 0) {
            throw new MapsServiceException("No route found");
        }
    }

    public String getTime() throws MapsServiceException {
        this.checkValidity();
        return this.getRows()[0].elements[0].duration.getText();
    }

    public String getDistance() throws MapsServiceException{
        this.checkValidity();
        return this.getRows()[0].elements[0].distance.getText();
    }

}


