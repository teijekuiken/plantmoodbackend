package com.oopa.domain.services;

import com.oopa.domain.MqttOutboundConfiguration;
import com.oopa.domain.model.PlantmoodHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class MqttService {

    @Autowired
    private PlantmoodHistoryService plantmoodHistoryService;

    @Autowired
    private MqttOutboundConfiguration config;

    private static Logger logger = LoggerFactory.getLogger(PlantmoodHistoryService.class);

    public void splitMessage(Message<?> incommingMessage){
        String[] splitMessage = incommingMessage.toString().split("[=,]");

        String arduinoSn = splitMessage[1];
        int moistureValue = Integer.parseInt(splitMessage[2]);

        PlantmoodHistory plantmoodHistory = new PlantmoodHistory();
        plantmoodHistory.setArduinoSn(arduinoSn);
        plantmoodHistory.setHealth(moistureValue);

        plantmoodHistoryService.addHistory(plantmoodHistory);
        logger.info("Received: ArduinoSn {} with moisturevalue of {}", plantmoodHistory.getArduinoSn(), plantmoodHistory.getHealth());
    }

    public void sendMoodToPlantMood(String arduinoSn, String mood){
        String outgoingTopic = "Plantmood/"+arduinoSn+"/Mood";
        config.mqttOutboundChannel().send(MessageBuilder.withPayload(mood).setHeader(MqttHeaders.TOPIC, outgoingTopic).build());
        logger.info("Published mood: {} to Plantmood: {}", mood, arduinoSn);
    }

}
