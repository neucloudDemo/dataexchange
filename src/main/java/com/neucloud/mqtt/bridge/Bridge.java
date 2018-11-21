package com.neucloud.mqtt.bridge;


import com.neucloud.mqtt.bridge.config.ApplicationProperties;
import com.neucloud.mqtt.bridge.config.ProcessProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bridge {
    private final Logger logger = LoggerFactory.getLogger(Bridge.class);

    private void connect() throws Exception {
        //加载properties配置
        ProcessProperties pr = new ProcessProperties();
        ApplicationProperties p = pr.processApplicationProperties();


        MqttSampleClient mqttClient = new MqttSampleClient(p);
        //mqtt连接
        mqttClient.connect();
        //订阅topic的消息
        mqttClient.SubscribeMsg(p.mqttTopic);
    }
    public static void main(String rags[]) throws Exception {
        Bridge bridge = new Bridge();
        bridge.connect();
    }
}
