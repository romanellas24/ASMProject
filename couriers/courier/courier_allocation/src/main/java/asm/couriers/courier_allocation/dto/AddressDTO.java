package asm.couriers.courier_allocation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Schema(description = "Represents a geographic address with latitude and longitude")
public class AddressDTO {
    @Schema(description = "Latitude of the address", example = "45.4642")
    private double lat;

    @Schema(description = "Longitude of the address", example = "9.1900")
    private double lng;

    public String toTupleString(){
        return String.format(Locale.US,"%f,%f", this.lat, this.lng);
    }
}
