package com.example.instrumentprices.service;

import com.example.instrumentprices.domain.InstrumentPrice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * Service responsible for publishing prices to downstream systems using publish subscribe design patter.
 */
@Service
public class PricesPublisher {

    private static final String PRICES_TOPIC = "prices-topic";
    private static Logger log = LoggerFactory.getLogger(PricesPublisher.class);

    private JmsTemplate jmsTopicTemplate;

    public PricesPublisher(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    /**
     * Sends message to topic.
     * @param price price object
     */
    public void publish(InstrumentPrice price) {
        log.info("sending price to jms topic");
        jmsTopicTemplate.convertAndSend(PRICES_TOPIC, price);
    }
}
