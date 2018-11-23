package com.neucloud.mqtt.bridge;

import com.alibaba.fastjson.JSONObject;
import com.neucloud.mqtt.bridge.config.ApplicationProperties;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MqttSampleClient implements IMqttMessageListener{

    private final Logger logger = LoggerFactory.getLogger(MqttSampleClient.class);

    private MqttClient mqttClient = null;
    private MqttClient targetMqttClient = null;
    private MqttConnectOptions connectionOptions = null;

    private static final int MQTT_VERSION = 4;
    private ApplicationProperties p;

    public MqttSampleClient(ApplicationProperties p) {
        this.p = p;

        String serverURI = p.mqttUri;
        String clientId = "abc"+System.currentTimeMillis();
        String userName = p.mqttUserName;
        if (serverURI.isEmpty() || clientId.isEmpty() || userName.isEmpty()) {
            throw new IllegalArgumentException();
        }
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            mqttClient = new MqttClient(serverURI, clientId, persistence);
            connectionOptions = new MqttConnectOptions();
            this.connectionOptions.setKeepAliveInterval(p.mqttKeepAliveInterval);
            this.connectionOptions.setCleanSession(p.mqttCleanSession);
            this.connectionOptions.setMqttVersion(MQTT_VERSION);
            this.connectionOptions.setUserName(userName);
            this.connectionOptions.setConnectionTimeout(p.mqttConnectionTimeout);

            if (userName != null && !userName.isEmpty()) {
                this.connectionOptions.setPassword(p.mqttPassword.toCharArray());
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void connect() throws MqttException {
        mqttClient.connect(connectionOptions);
        logger.info("mqtt  connect success !");
    }

    public void SubscribeMsg(String mqttTopic){
        try {
            mqttClient.subscribe(mqttTopic, 1,this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    //mqtt接收消息
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        JSONObject msg= JSONObject.parseObject(mqttMessage.toString());
        System.out.println("接收消息内容 : " + msg);

        //做数据逻辑判断
        Object value = msg.get("value");
        if(value!=null){
            if(Double.parseDouble(value.toString())>0){
                sendMsg(msg);
            }
        }else{
            System.out.println("mqtt接收到的消息中没有value属性，直接丢弃");
        }

    }

    public void sendMsg(JSONObject jsonMsg) throws MqttException {
        targetMqttClient =new MqttClient(p.mqttUri, System.currentTimeMillis()+"qaz", new MemoryPersistence());
        targetMqttClient.connect(connectionOptions);
        if(targetMqttClient != null && jsonMsg != null){
            byte[] content = jsonMsg.toJSONString().getBytes();
            MqttMessage message =new MqttMessage(content);
            message.setQos(1);
            try {
                targetMqttClient.publish(this.p.targetMqttTopic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}
