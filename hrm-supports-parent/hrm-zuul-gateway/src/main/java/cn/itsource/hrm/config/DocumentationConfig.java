package cn.itsource.hrm.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO
 * @Author: LY
 * @Date: 2019-12-24 19:53
 * @Version: v1.0
 **/
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider{@Override
public List<SwaggerResource> get() {
    List resources = new ArrayList<>();
    resources.add(swaggerResource("系统管理", "/services/system/v2/api-docs", "1.0"));
    resources.add(swaggerResource("课程中心", "/services/course/v2/api-docs", "1.0"));
    resources.add(swaggerResource("文件服务","/services/file/v2/api-docs","1.0"));
    return resources;
}

    private SwaggerResource swaggerResource(String name, String location,
            String version) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }


}
