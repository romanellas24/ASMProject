package api.workers;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Worker {

    public final String localName;
    public final ObjectMapper objectMapper;

    public Worker(String localName, ObjectMapper objectMapper) {
        this.localName = localName;
        this.objectMapper = objectMapper;
    }

    protected boolean isJobForThisWorker(final ActivatedJob job) {
        final String jobRestaurant = objectMapper.convertValue(job.getVariablesAsMap().get("restaurant"), String.class);

        if (jobRestaurant == null) {
            log.warn("Worker [{}]: Job {} received without 'restaurantId' variable. Ignoring.", localName, job.getKey());
            return false;
        }

        return localName.equals(jobRestaurant);
    }

    protected void ignoreJob(JobClient client, final ActivatedJob job) {
        final String jobRestaurantId = (String) job.getVariablesAsMap().get("restaurantId");
        log.warn("Worker [{}]: Ignoring job intended for '{}' (ID: {})", localName, jobRestaurantId, job.getKey());
        client.newFailCommand(job.getKey())
                .retries(job.getRetries())
                .errorMessage("Job not intended for worker " + localName)
                .send()
                .join();
    }
}
