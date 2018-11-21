package com.neucloud.mqtt.bridge.config;

import com.google.common.io.Resources;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

public class ProcessProperties {

    public ApplicationProperties processApplicationProperties() throws IllegalAccessException, InstantiationException {
        ApplicationProperties object = ApplicationProperties.class.newInstance();
        Field[] fields = FieldUtils.getAllFields(ApplicationProperties.class);
        for (Field field : fields) {
            Key key = field.getAnnotation(Key.class);
            String defKey = key.value();
            Object value = getValue(defKey);
            if (value != null) {
                if (field.getType() == Integer.class) {
                    value = Integer.valueOf(value.toString());
                } else if (field.getType() == Boolean.class) {
                    value = Boolean.valueOf(value.toString());
                }
                FieldUtils.writeDeclaredField(object, field.getName(), value, true);
            }
        }
        return object;
    }

    private Object getValue(String key) {
        try {
            Object value = getEnv(key);
            if (value == null) {
                InputStream props = Resources.getResource("application.properties").openStream();
                Properties properties = new Properties();
                properties.load(props);
                if (properties.containsKey(key)) {
                    return properties.getProperty(key);
                }
            }else{
                return value;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Object getEnv(String key) {
        Map<String, String> envs = System.getenv();
        key = key.replace(".", "_").toUpperCase();
        return envs.get(key);
    }

}
