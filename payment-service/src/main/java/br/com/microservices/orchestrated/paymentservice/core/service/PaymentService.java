package br.com.microservices.orchestrated.paymentservice.core.service;

import br.com.microservices.orchestrated.paymentservice.core.dto.Event;
import br.com.microservices.orchestrated.paymentservice.core.dto.OrderProduct;
import br.com.microservices.orchestrated.paymentservice.core.model.Payment;
import br.com.microservices.orchestrated.paymentservice.core.producer.KafkaProducer;
import br.com.microservices.orchestrated.paymentservice.core.repository.PaymentRepository;
import br.com.microservices.orchestrated.paymentservice.core.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentService {

    private static final String CURRENT_SOURCE = "PAYMENT_SERVICE";

    private final JsonUtil jsonUtil;
    private final KafkaProducer producer;
    private final PaymentRepository paymentRepository;

    public void realizePayment(Event event) {
        try {
            checkCurrentValidation(event);
            createPendingPayment(event);
            handleSuccess(event);
        } catch (Exception e) {
            log.error("Error realizing payment", e);
            handleFailCurrentNotExecuted(event, e.getMessage());
        }
        producer.sendEvent(jsonUtil.toJson(event));
    }

    private void checkCurrentValidation(Event event) {
        if (paymentRepository.existsByOrderIdAndTransactionId(event.getOrderId(), event.getTransactionId())) {
            throw new ValidateException("There's another transactionId for this validation.");
        }
    }

    private void createPendingPayment(Event event) {
        var payment = Payment.builder()
                .orderId(event.getOrder().getId())
                .transactionId(event.getTransactionId())
                .totalItems(calculateTotalItems(event))
                .totalAmount(calculateAmount(event))
                .build();
        save(payment);
        setEventAmountItems(event, payment);
    }

    private void save(Payment payment) {
        paymentRepository.save(payment);
    }

    // metodo que vai calcular o valor total da compra, o reduce vai somar o valor de cada produto e incrementar
    private double calculateAmount(Event event) {
       return event.getOrder().getProducts().stream()
               .map(product -> product.getQuantity() * product.getProduct().getUnitValue())
                .reduce(0.0, Double::sum);
    }

    private int calculateTotalItems(Event event) {
        return event.getOrder().getProducts().stream()
                .map(OrderProduct::getQuantity)
                .reduce(0, Integer::sum);
    }

    private void setEventAmountItems(Event event, Payment payment) {
        event.getOrder().setTotalAmount(payment.getTotalAmount());
        event.getOrder().setTotalItems(payment.getTotalItems());
    }
}
