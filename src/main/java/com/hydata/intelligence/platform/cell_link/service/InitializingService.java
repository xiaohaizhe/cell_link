package com.hydata.intelligence.platform.cell_link.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author: Jasmine
 * @createTime: 2019-10-09 15:42
 * @description:
 * @modified:
 */
@Component
public class InitializingService implements CommandLineRunner {

    @Autowired
    private MqttInitService mqttInitService;
    @Value("${spring.data.mongodb.uri}")
    private String mongouri;

    @Value("${spring.datasource.url}")
    private String mysqlurl;


    private static Logger logger = LogManager.getLogger(InitializingService.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info("MongoDB数据库地址：");
        logger.info(mongouri);
        logger.info("mysql数据库地址：");
        logger.info(mysqlurl);

        mqttInitService.init();
    }

}
