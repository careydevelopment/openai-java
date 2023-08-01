package us.careydevelopment.ai.openai.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class AiProperties {

    private static Logger LOG = LoggerFactory.getLogger(AiProperties.class);

    private static final String PROPERTIES_FILE_NAME = "application.properties";

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            PROPERTIES.load(classloader.getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (Exception e) {
            LOG.error("Problem fetching properties!", e);
        }
    }

    public static String get(final String key) {
        final String value = (String)PROPERTIES.get(key);
        return value;
    }
}
