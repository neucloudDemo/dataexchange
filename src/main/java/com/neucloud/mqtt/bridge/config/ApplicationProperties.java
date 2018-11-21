package com.neucloud.mqtt.bridge.config;


public class ApplicationProperties {

    @Key("mqtt.connectionTimeout")
    public Integer mqttConnectionTimeout = 30;
    @Key("mqtt.keepAliveInterval")
    public Integer mqttKeepAliveInterval = 60;
    @Key("mqtt.cleanSession")
    public Boolean mqttCleanSession = true;
    @Key("mqtt.password")
    public String mqttPassword;
    @Key("mqtt.userName")
    public String mqttUserName;
    @Key("mqtt.uri")
    public String mqttUri;
    @Key("mqtt.topic")
    public String mqttTopic;
    @Key("mqtt.clientId")
    public String mqttClientId;
    @Key("mqtt.targetTopic")
    public String targetMqttTopic;

}
