package cn.itsource.hrm.mapper;

import cn.itsource.hrm.domain.RolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LY
 * @since 2019-12-29
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    void insertBatch(List<RolePermission> list);
}
