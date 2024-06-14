package com.mfc.infra.controller;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArqLoggerRestController {
    Logger logger = LoggerFactory.getLogger(ArqLoggerRestController.class);
    @GetMapping(value = "log")
    public void tratarLog(@RequestParam @NotNull String msg) {
        logger.info("Mensaje recibido: " + msg);
        //mandar a Elasticsearch, o Kafka o ...
    }


}

