package br.com.microservices.orchestrated.orchestratorservice.core.saga;

import br.com.microservices.orchestrated.orchestratorservice.core.dto.Event;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;
import br.com.microservices.orchestrated.orderservice.config.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
@Component
@AllArgsConstructor
public class SagaExecutionController {


    public ETopics getNextTopic(Event event) {
        if(isEmpty(event.getStatus()) || isEmpty(event.getSource())) {
           throw new ValidationException("Event status or source is empty and must be informed!");
        }
        return ETopics.BASE_ORCHESTRATOR;
    }
}
