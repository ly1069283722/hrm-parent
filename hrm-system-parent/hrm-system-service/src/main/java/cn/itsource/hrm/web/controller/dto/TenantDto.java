package cn.itsource.hrm.web.controller.dto;

import cn.itsource.hrm.domain.Tenant;
import lombok.Data;
import lombok.ToString;

/**
 * @Description 租户注册的参数接收
 * @Author LY
 * @Date 2019/12/29 15:54
 * @Version v1.0
 **/
@Data
@ToString
public class TenantDto {

    private Tenant tenant;

    private String username;

    private String password;

    private Long meal;

}
