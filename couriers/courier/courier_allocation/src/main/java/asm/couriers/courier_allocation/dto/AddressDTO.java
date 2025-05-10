package asm.couriers.courier_allocation.dto;

import lombok.*;

import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AddressDTO {
    private double lat;
    private double lng;

    public String toTupleString(){
        return String.format(Locale.US,"%f,%f", this.lat, this.lng);
    }
}
