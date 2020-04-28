package io.github.firefang.mock.server.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "io.github.firefang.mock.server.mapper")
public class MybatisConfig {

}
