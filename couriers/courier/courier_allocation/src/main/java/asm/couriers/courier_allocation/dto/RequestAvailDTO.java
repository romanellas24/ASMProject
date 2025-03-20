package asm.couriers.courier_allocation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class RequestAvailDTO {
    private AddressDTO restAddr;
    private AddressDTO clientAddr;
    //add datetime
}
