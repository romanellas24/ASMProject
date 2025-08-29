package asm.couriers.courier_allocation.impl;

import asm.couriers.courier_allocation.dto.AvailabilityDTO;
import asm.couriers.courier_allocation.dto.DistanceMatrixResponseDTO;
import asm.couriers.courier_allocation.exception.MapsServiceException;
import asm.couriers.courier_allocation.service.MapsService;

import asm.couriers.courier_allocation.utils.Const;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Objects;


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

    /*
    https://api.distancematrix.ai/maps/api/distancematrix/json?origins=Westminster Abbey, 20 Deans Yd, Westminster, London SW1P 3PA, United Kingdom&destinations=St John's Church, North End Rd, Fulham, London SW6 1PB, United Kingdom&key=<your_access_token>
     */
    @Override
    public AvailabilityDTO getInfoDelivery(String restAddr, String clientAddr) throws MapsServiceException {

        AvailabilityDTO availDTOTimeDistance = new AvailabilityDTO();

        ResponseEntity<DistanceMatrixResponseDTO> result =distanceMatrixFast.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("origins", restAddr)
                        .queryParam("destinations", clientAddr)
                        .queryParam("key", this.DISTANCE_MATRIX_API_KEY)
                        .build()
                )
                .retrieve()
                .toEntity(DistanceMatrixResponseDTO.class);

        String time =  Objects.requireNonNull(result.getBody()).getTime();
        String distance = result.getBody().getDistance();

        availDTOTimeDistance.setTime(Integer.parseInt(time.split(" ")[0]));
        availDTOTimeDistance.setDistance(Double.parseDouble(distance.split(" ")[0]));

        return availDTOTimeDistance;
    }
}
