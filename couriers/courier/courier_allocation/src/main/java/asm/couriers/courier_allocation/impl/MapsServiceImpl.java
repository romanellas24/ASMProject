package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dto.AddressDTO;
import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import asm.couriers.courier_allocation.dto.DistanceMatrixResponseDTO;
import asm.couriers.courier_allocation.exception.MapsServiceException;
import asm.couriers.courier_allocation.service.MapsService;

import asm.couriers.courier_allocation.utils.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class MapsServiceImpl implements MapsService {

    RestClient distanceMatrixFast;

    @Value("${distancematrix.fast.key}")
    private String DISTANCE_MATRIX_API_KEY;

    public MapsServiceImpl() {
        this.distanceMatrixFast = RestClient.builder()
                .baseUrl(Const.DISTANCE_MATRIX_BASE_URL)
                .build();
    }

    @Override
    public AvailabilityDTO getInfoDelivery(AddressDTO restAddr, AddressDTO clientAddr) throws MapsServiceException, Exception {

        AvailabilityDTO availDTOTimeDistance = new AvailabilityDTO();

        ResponseEntity<DistanceMatrixResponseDTO> result =distanceMatrixFast.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("origins", restAddr.toTupleString())
                        .queryParam("destinations", clientAddr.toTupleString())
                        .queryParam("key", this.DISTANCE_MATRIX_API_KEY)
                        .build()
                )
                .retrieve()
                .toEntity(DistanceMatrixResponseDTO.class);

        String time =  result.getBody().getTime();
        String distance = result.getBody().getDistance();

        availDTOTimeDistance.setTime(Integer.parseInt(time.split(" ")[0]));
        availDTOTimeDistance.setDistance(Double.parseDouble(distance.split(" ")[0]));

        return availDTOTimeDistance;
    }
}
