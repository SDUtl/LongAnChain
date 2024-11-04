package org.davex.chainmaker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.davex.chainmaker.config.InitClient.initChainClientForPk;

@SpringBootApplication
public class LongAnChainApplication {

    public static void main(String[] args) {
        SpringApplication.run(LongAnChainApplication.class, args);

    }
}
