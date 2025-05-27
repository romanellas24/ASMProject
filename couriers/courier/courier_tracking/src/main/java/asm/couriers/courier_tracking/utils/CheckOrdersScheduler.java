package asm.couriers.courier_tracking.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Configuration
public class CheckOrdersScheduler extends ThreadPoolTaskScheduler {

    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Bean
    public CheckOrdersScheduler taskScheduler() {
        return new CheckOrdersScheduler();
    }

    public void schedule(Runnable task, Trigger trigger, Integer courier_id){
        ScheduledFuture<?> future = super.schedule(task, trigger);
        scheduledTasks.put(courier_id, future);
    }

    public void cancelSchedule(Integer courier_id){
        ScheduledFuture<?> future = scheduledTasks.get(courier_id);
        if (future != null) {
            future.cancel(true);
            scheduledTasks.remove(courier_id);
        }
    }
}
