package com.hydata.intelligence.platform.cell_link;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CellLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(CellLinkApplication.class, args);
    }

}
