package com.twins.clone.common.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
public class LogController {


    @GetMapping("/log/test")
    public String logTest() {
        try {
            log.info("테스트");
            throw new IllegalArgumentException("에러 테스트");
        } catch (IllegalArgumentException e) {
            log.error("에러 발생", e);
        }

        return "OK";
    }
}
