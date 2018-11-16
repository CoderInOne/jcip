package xunshan.jcip.ch12.log_service;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        props.load(App.class.getResourceAsStream("/my.properties"));
        PropertyConfigurator.configure(props);
        Logger logger = LoggerFactory.getLogger(App.class);
        logger.info(logger.getClass().getName());
        logger.info("hi");
    }
}
