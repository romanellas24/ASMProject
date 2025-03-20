package asm.couriers.courier_allocation.utils;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Const {

    public static final String DISTANCE_MATRIX_BASE_URL = "https://api.distancematrix.ai/maps/api/distancematrix/json";
    public static final Double PRICE_PER_KM = 0.3;
}
