package com.example.easyrent.config;

import com.example.easyrent.utils.AcreageUtils;
import com.example.easyrent.utils.MoneyUtils;
import com.example.easyrent.utils.TimeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;


@Configuration
public class ThymeleafConfig {
    @Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }

    @Bean
    public TimeUtils timeUtils() {
        return new TimeUtils();
    }

    @Bean
    public MoneyUtils moneyUtils() {
        return new MoneyUtils();
    }

    @Bean
    public AcreageUtils acreageUtils() {
        return new AcreageUtils();
    }
}
