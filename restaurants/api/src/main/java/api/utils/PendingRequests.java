package api.utils;

import api.dto.ResponseOrderDTO;
import api.dto.WaitingOrderDTO;
import api.service.RabbitService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingRequests {

    private final Map<String, Pair<CompletableFuture<ResponseOrderDTO>, WaitingOrderDTO>> requests = new ConcurrentHashMap<>();
    private final RabbitService rabbitService;

    public PendingRequests(RabbitService rabbitService) {
        this.rabbitService = rabbitService;
    }

    public void put(String correlationId, CompletableFuture<ResponseOrderDTO> future, WaitingOrderDTO waitingOrderDTO) {
        requests.put(correlationId, Pair.of(future, waitingOrderDTO));
    }

    public Boolean checkExists(String correlationId) {
        return requests.containsKey(correlationId);
    }

    public WaitingOrderDTO getWaitingOrder(String correlationId) {
        return requests.get(correlationId).getSecond();
    }

    public void complete(String correlationId, ResponseOrderDTO result) {
        CompletableFuture<ResponseOrderDTO> future = requests.remove(correlationId).getFirst();
        if (result.isAccepted()){
            rabbitService.publishNewOrder(result.getOrder().getId());
        }
        future.complete(result);
    }

    public void timeout(String correlationId) {
        Pair<CompletableFuture<ResponseOrderDTO>, WaitingOrderDTO> tmp = requests.remove(correlationId);
        if(tmp != null) {
            CompletableFuture<ResponseOrderDTO> future = tmp.getFirst();
            future.complete(new ResponseOrderDTO(false,null));
        }
    }
}

