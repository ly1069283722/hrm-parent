package cn.itsource.hrm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Description: TODO
 * @Author: LY
 * @Date: 2019-12-30 16:50
 * @Version: v1.0
 **/
@SpringBootApplication
@EnableEurekaClient
public class FileServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FileServiceApplication.class,args);
    }
}
