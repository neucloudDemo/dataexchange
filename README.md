#### 1.环境变量定义

必填

STANDALONE  :   true 

mqtt

1. MQTT_CLIENTID
2. MQTT_USERNAME
3. MQTT_PASSWORD
4. MQTT_URI
5. MQTT_TOPIC
6. MQTT_TARGETTOPIC
 



非必填

1.   connectionTimeout

   > connection timeout m 默认值 为 30s.
   
2.  MQTT_KEEPALIVEINTERVAL

>    "keep alive" interval  默认 60s 
3.  cleanSession
    
main方法：
    com.neucloud.mqtt.bridge.Bridge
    
    
启动main方法之后，
    1.加载properties配置
    2.mqtt连接
    3.mqtt订阅消息
MqttClient类自动接收消息（messageArrived），进行数据判断，将符合条件的数据转发到另一个topic中。

     

      
          
