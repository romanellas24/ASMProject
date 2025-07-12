package api.workers;

import api.dto.CreateOrderDTO;
import api.dto.MenuDTO;
import api.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;

@Component
@Slf4j
public class GeneralJobWorker extends Worker {

    private final DayOfWeek closingDay;
    private final MenuService menuService;


    public GeneralJobWorker(@Value("${local.closingDay}") int closingDayValue,
                            @Value("${local.server.name}") String localName,
                            ObjectMapper objectMapper,
                            MenuService menuService) {
        super(localName, objectMapper);
        this.closingDay = DayOfWeek.of(closingDayValue);
        this.menuService = menuService;
    }

    @JobWorker(type = "is_local_closed")
    public void addBoolIsLocalClosedToVariables(JobClient client, final ActivatedJob job){

        if (!isJobForThisWorker(job)){
            ignoreJob(client, job);
            return;
        }

        log.info("check if local is closed today.");
        Boolean isTodayClosed = LocalDate.now().getDayOfWeek() == closingDay;

        try{
            client.newCompleteCommand(job.getKey())
                    .variable("closed", isTodayClosed)
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure retrieving closing day.");
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }

    @JobWorker(type = "prepare_menu_send")
    public void prepareMenuSend(JobClient client, final ActivatedJob job){
        if (!isJobForThisWorker(job)){
            ignoreJob(client, job);
            return;
        }

        log.info("prepare menu object to send request to acmeat");
        try {
            MenuDTO menu = this.menuService.getMenu(LocalDate.now());
            String localUrl = "https://" + localName + ".romanellas.cloud";
            client.newCompleteCommand(job.getKey())
                    .variables(Map.of("menu", menu, "url", localUrl))
                    .send()
                    .join();

        } catch (Exception e) {
            log.error("Failure getting menu.");
            client.newFailCommand(job.getKey())
                    .retries(job.getRetries() - 1)
                    .errorMessage(e.getMessage())
                    .send()
                    .join();
        }
    }
}
