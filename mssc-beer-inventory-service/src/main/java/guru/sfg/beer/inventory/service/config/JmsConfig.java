package guru.sfg.beer.inventory.service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class JmsConfig {

    //public static final String BREWING_REQUEST_QUEUE = "brewing-request";
    public static final String NEW_INVENTORY_QUEUE = "new-inventory";
    //public static final String VALIDATE_ORDER_QUEUE = "validate-order";
    //public static final String VALIDATE_ORDER_RESPONSE_QUEUE = "validate-order-response";
    public static final String ASIGNAR_ORDEN_QUEUE = "asignar-orden";
    public static final String ASIGNAR_ORDEN_RESPONSE_QUEUE = "asignar-orden-response";
    public static final String DESASIGNAR_ORDEN_QUEUE = "desasignar-orden";

    @Bean // Serialize message content to json using TextMessage
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}