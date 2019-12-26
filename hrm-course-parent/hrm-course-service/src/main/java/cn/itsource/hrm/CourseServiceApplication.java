package cn.itsource.hrm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: TODO
 * @Author: LY
 * @Date: 2019-12-26 09:46
 * @Version: v1.0
 **/

@SpringBootApplication
@MapperScan("cn.itsource.hrm.mapper")
@EnableTransactionManagement
@EnableEurekaClient
@EnableSwagger2 //开启swagger
public class CourseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CourseServiceApplication.class,args);
    }
}
