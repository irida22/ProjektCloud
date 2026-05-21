package com.student.management.mapper.config;

import com.student.management.mapper.StudentMapper;
import com.student.management.mapper.StudentMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {

    @Bean
    public StudentMapper studentMapper() {
        return new StudentMapperImpl();
    }
}
