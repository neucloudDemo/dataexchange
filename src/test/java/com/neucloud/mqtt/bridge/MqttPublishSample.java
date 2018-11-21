package com.neucloud.mqtt.bridge;

import com.google.common.base.Stopwatch;
import com.neucloud.mqtt.bridge.config.ApplicationProperties;
import com.neucloud.mqtt.bridge.config.ProcessProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MqttPublishSample {

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, MqttException {

        ProcessProperties pr = new ProcessProperties();
        ApplicationProperties p = pr.processApplicationProperties();

        String topic = p.mqttTopic;
        int qos = 0;
        String broker = p.mqttUri;
        String clientId = p.mqttClientId +System.currentTimeMillis();
        MqttClientPersistence persistence = new MemoryPersistence();
        AtomicInteger ai = new AtomicInteger(1);

        try {

            final MqttAsyncClient sampleClient = new MqttAsyncClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setUserName(p.mqttUserName);
            connOpts.setPassword(p.mqttPassword.toCharArray());
            connOpts.setCleanSession(true);
            connOpts.setMqttVersion(4);
            connOpts.setMaxInflight(65535);
            connOpts.setKeepAliveInterval(1200);
            connOpts.setKeepAliveInterval(30);

            System.out.println("Connecting to broker: " + broker);

            IMqttToken iMqttToken = sampleClient.connect(connOpts);
            iMqttToken.waitForCompletion();

            System.out.println("Connected");

            String content = "===start===";
            MqttMessage message = new MqttMessage(content.getBytes());


            Stopwatch stopwatch = Stopwatch.createStarted();

            for (int i = 0; i < 10; i++) {
                Thread.sleep(5);
                ai.getAndIncrement();
                content = "a, " + System.currentTimeMillis();
                message = new MqttMessage(content.getBytes());
                message.setQos(qos);
                try {
                    sampleClient.publish(topic, message);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }


            stopwatch.stop();

            System.out.println("==== send  count  " + ai.intValue());
            content = "===stop===";
            message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.err.println("Ops " + ": " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
            System.out.println("Message published");

            System.out.println("Disconnected");
            System.exit(0);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }
}
