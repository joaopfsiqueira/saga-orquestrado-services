package br.com.microservices.orchestrated.orchestratorservice.core.saga;

import br.com.microservices.orchestrated.orchestratorservice.core.enums.EEventSource;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ESagaStatus;
import br.com.microservices.orchestrated.orchestratorservice.core.enums.ETopics;

// classe utilitária, por isso final. Quando eu digo que é final eu estou dizendo que não pode ser extendida, então só vai ter métodos estáticos
public final class SagaHandler {

    private SagaHandler() {

    }

    // matriz de objetos, onde cada objeto é um array de objetos
    public static final Object[][] SAGA_HANDLER = {
            {EEventSource.ORCHESTRATOR, ESagaStatus.SUCCESS, ETopics.PRODUCT_VALIDATION_SUCCESS},
            {1, 2, 1},
            {2, 2, 1}
    };
}
