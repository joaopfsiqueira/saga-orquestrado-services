package br.com.microservices.orchestrated.productvalidationservice.core.consumer;

import br.com.microservices.orchestrated.orderservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class ProductValidationConsumer {

    private final JsonUtil jsonUtil;

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-validation-success}"
    ) //esse metodo é um kafka listerner! uma função que consome do kafka
    public void ConsumeProductValidationSuccessEvent(String payload) {
        log.info("Receiving success event {} from product-validation-success topic", payload);
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }

    @KafkaListener(
            groupId = "${spring.kafka.consumer.group-id}",
            topics = "${spring.kafka.topic.product-validation-failure}"
    ) //esse metodo é um kafka listerner! uma função que consome do kafka
    public void ConsumeProductValidationFailureEvent(String payload) {
        log.info("Receiving failure event {} from product-validation-failure topic", payload);
        var event = jsonUtil.toEvent(payload);
        log.info(event.toString());
    }


}
