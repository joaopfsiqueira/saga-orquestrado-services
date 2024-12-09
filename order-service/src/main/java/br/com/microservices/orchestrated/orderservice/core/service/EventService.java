package br.com.microservices.orchestrated.orderservice.core.service;

import br.com.microservices.orchestrated.orderservice.core.document.Event;
import br.com.microservices.orchestrated.orderservice.core.repository.EventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository repository;

    public void  notifyEnding(Event event){
        // resetando algumas informações para caso tenha perdido
        event.setOrderId(event.getOrderId());
        event.setCreatedAt(LocalDateTime.now());
        save(event);
        log.info("Order {} has been notified! TransactionId: {}", event.getOrderId(), event.getTransactionId());
    }

    public Event save(Event event) {
        return repository.save(event);
    }
}
