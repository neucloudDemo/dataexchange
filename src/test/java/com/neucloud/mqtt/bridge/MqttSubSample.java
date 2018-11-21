package com.neucloud.mqtt.bridge;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.atomic.AtomicInteger;

public class MqttSubSample {

    public static void main(String rags[]) throws MqttException {

        String topic = "";
        int qos = 0;
        String broker = "";
        String clientId = "";
        MemoryPersistence persistence = new MemoryPersistence();

        MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
        MqttConnectOptions connOpts = new MqttConnectOptions();
        connOpts.setUserName("");
        connOpts.setPassword("".toCharArray());
        connOpts.setCleanSession(true);
        connOpts.setMqttVersion(4);
        System.out.println("Connecting to broker: " + broker);


        AtomicInteger ai = new AtomicInteger(1);

        IMqttToken iMqttToken = sampleClient.connect(connOpts);
        iMqttToken.waitForCompletion();
        System.out.println("Connected");


        sampleClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                System.out.println("连接断开，可以做重连");
            }

            @Override
            public void messageArrived(String s, MqttMessage message) throws Exception {
//                System.out.println("接收消息主题 : " + topic);
//                System.out.println("接收消息Qos : " + message.getQos());
                System.out.println("接收消息内容 : " + new String(message.getPayload()));
                System.out.println("==count==" + ai.getAndAdd(1));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        sampleClient.subscribe(topic, qos);
    }
}
