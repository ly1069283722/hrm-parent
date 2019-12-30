package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.domain.*;
import cn.itsource.hrm.mapper.*;
import cn.itsource.hrm.service.ITenantService;
import cn.itsource.hrm.web.controller.dto.TenantDto;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LY
 * @since 2019-12-29
 */
@Service
@Transactional
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

    @Autowired
    private TenantMealMapper tenantMealMapper;
    @Autowired
    private MealPermissionMapper mealPermissionMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private EmployeeRoleMapper employeeRoleMapper;

    @Override
    public void registTenant(TenantDto tenantDto) {

        //保存租户表数据,返回租户id
        Tenant tenant = tenantDto.getTenant();
        //设置初始值
        tenant.setState(0);
        tenant.setRegisterTime(System.currentTimeMillis());
        baseMapper.insert(tenant);
        //添加租套餐中间表数据
        TenantMeal tenantMeal = new TenantMeal();
        tenantMeal.setTenantId(tenant.getId());
        tenantMeal.setMealId(tenantDto.getMeal());
        //设置初始值
        tenant.setState(0);
        tenant.setRegisterTime(System.currentTimeMillis()+5*1000*24*60*60 );
        tenantMealMapper.insert(tenantMeal);

        //通过套餐id查询权限id
        List<MealPermission> mealPermissions = mealPermissionMapper.selectList(
                new QueryWrapper<MealPermission>()
                        .eq("meal_id", tenantDto.getMeal())
        );
        //创建租户管理员角色获取id
        Role role = new Role();
        role.setSn("TenantAdmin");
        role.setName("租户管理员");
        role.setTenant(tenant.getId());
        roleMapper.insert(role);
        //把角色和权限的id存入角色权限中间表中
        List<RolePermission> list=new ArrayList<>();

        for (MealPermission mealPermission : mealPermissions) {
            Long permissionId = mealPermission.getPermissionId();
            RolePermission rp = new RolePermission();
            rp.setRoleId(role.getId());
            rp.setPermissionId(permissionId);
            list.add(rp);
        }
        if (list!=null&&list.size()>0){
            rolePermissionMapper.insertBatch(list);
        }

        //创建员工,分配角色,租户管理员
        Employee employee = new Employee();
        employee.setUsername(tenantDto.getUsername());
        employee.setPassword(tenantDto.getPassword());
        employee.setState(0);
        employee.setInputTime(System.currentTimeMillis());
        employee.setTenantId(tenant.getId());
        employee.setType(3);
        employeeMapper.insert(employee);

        //添加员工角色中间表
        EmployeeRole employeeRole = new EmployeeRole();
        employeeRole.setEmployeeId(employee.getId());
        employeeRole.setRoleId(role.getId());
        employeeRoleMapper.insert(employeeRole);
    }
}
