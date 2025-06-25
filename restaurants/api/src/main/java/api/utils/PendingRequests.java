package api.utils;

import api.dto.ResponseOrderDTO;
import api.exception.TimeoutException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingRequests {

    // maps order_id -> response (future)
    private final Map<Integer, CompletableFuture<ResponseOrderDTO>> requests = new ConcurrentHashMap<>();

    public void put(CompletableFuture<ResponseOrderDTO> future, Integer id) {
        requests.put(id, future);
    }

    public Boolean checkExists(Integer id) {
        return requests.containsKey(id);
    }

    public void complete(Integer id, ResponseOrderDTO result) {
        CompletableFuture<ResponseOrderDTO> future = requests.remove(id);
        future.complete(result);
    }

    public void timeout(Integer id) {
        CompletableFuture<ResponseOrderDTO> future = requests.remove(id);
        if(future != null) {
            future.completeExceptionally(new TimeoutException("restaurant doesn't answer in 3 minutes"));
        }
    }
}

