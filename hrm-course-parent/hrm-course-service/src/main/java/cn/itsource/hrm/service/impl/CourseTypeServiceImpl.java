package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.domain.CourseType;
import cn.itsource.hrm.mapper.CourseTypeMapper;
import cn.itsource.hrm.service.ICourseTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程目录 服务实现类
 * </p>
 *
 * @author LY
 * @since 2019-12-26
 */
@Service
public class CourseTypeServiceImpl extends ServiceImpl<CourseTypeMapper, CourseType> implements ICourseTypeService {




    @Override
    public List<CourseType> addtree() {

        //List<CourseType> courseTypes = getByParentId2();
        List<CourseType> courseTypes = getByParentId();
        return courseTypes;
    }

    /**
     * Map➕循环的方式
     */
    public List<CourseType> getByParentId() {
        //初始化一个集合存放一级菜单
        List<CourseType> firstCourseType = new ArrayList<>();
        //查询数据库中的所有类型
        List<CourseType> allCourseType=baseMapper.selectList(null);

        //创建一个Map,把查询出来的所有类型存到Map中
        Map<Long,CourseType> map=new HashMap<>();
        for (CourseType courseType : allCourseType) {
            map.put(courseType.getId(), courseType);
        }

        for (CourseType courseType : allCourseType) {
            //如果是一级菜单就放入firstCourseType
                if(courseType.getPid().longValue()==0L){
                    firstCourseType.add(courseType );
                }else{
                //找到父级类型
                CourseType parent = map.get(courseType.getPid());
                if (parent!=null){
                    parent.getChildern().add(courseType);
                }
            }
        }

        return firstCourseType;
    }

    /**
     * 循环数组方式
     * @return
     */
    public List<CourseType> getByParentId2(){
        //初始化一个集合存放一级菜单
        List<CourseType> firstCourseType = new ArrayList<>();
        //查询数据库中的所有类型
        List<CourseType> allCourseType=baseMapper.selectList(null);

        for (CourseType courseType : allCourseType) {
            //如果是一级菜单就放入firstCourseType
            if(courseType.getId().longValue()==0L){
                firstCourseType.add(courseType );
            }else{
                //如果不是一级菜单就找到对应的一级菜单,然后放入一级菜单的Children中
                for (CourseType  parent : allCourseType) {
                    if(courseType.getId().longValue()==parent.getId().longValue()){
                        parent.getChildern().add(courseType);
                    }
                }
            }

        }

        return firstCourseType;
    }
}
