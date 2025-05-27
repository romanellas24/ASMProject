package asm.couriers.courier_allocation.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Const {

    public static final String DISTANCE_MATRIX_BASE_URL = "https://api.distancematrix.ai/maps/api/distancematrix/json";

    public static Double PRICE_PER_KM;

    @Value("${const.priceperkm}")
    private Double pricePerKm;

    @PostConstruct
    public void init() {
        PRICE_PER_KM = pricePerKm;
    }
}
