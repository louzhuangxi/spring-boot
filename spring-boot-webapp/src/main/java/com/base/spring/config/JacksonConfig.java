package com.base.spring.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * Description : TODO(jackson 配置)
 * User: h819
 * Date: 2016-10-14
 * Time: 0:04
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();

        /**
         * maven 引入 jackson-datatype-jsr310
         * 输出时间配置
         * @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
         * LocalDateTime modifiedDate;
         */
        objectMapper.registerModule(new JavaTimeModule()); // 由于 jpa 2.1 不支持 LocalDate , 转换为 jackson 时，也不支持，只能自定义转换
        //==
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true); // pretty print

        return objectMapper;

    }

}
