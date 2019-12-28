package cn.itsource.hrm.service.impl;

import cn.itsource.hrm.client.RedisClient;
import cn.itsource.hrm.domain.CourseType;
import cn.itsource.hrm.mapper.CourseTypeMapper;
import cn.itsource.hrm.service.ICourseTypeService;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
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

    @Autowired
    private RedisClient redisClient;

    private final String COURSE_TYPE="hrm:course:COURSE_TYPE";

    @Override
    public List<CourseType> addtree() {

        //List<CourseType> courseTypes = getByParentId2();
        //List<CourseType> courseTypes = getByParentId();
        String courseTypeStr = redisClient.get(COURSE_TYPE);
        List<CourseType> list=null;

        if(StringUtils.isEmpty(courseTypeStr)){
            //存在,json字符串转为java对象集合
            JSONObject.parseArray(courseTypeStr,CourseType.class);

            System.out.println("存在.......");
        }else {
            //不存在,查询数据库
            list=getByParentId();
            //list集合转json字符串
            String jsonStr = JSONObject.toJSONString(list);
            //放入redis中
            redisClient.set(COURSE_TYPE, jsonStr);
            System.out.println("不存在");
        }
        //返回数据
        return list;
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
                    parent.getChildren().add(courseType);
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
                        parent.getChildren().add(courseType);
                    }
                }
            }

        }

        return firstCourseType;
    }

/***
 * 增删改同步
 */

    private void syncOperate() {
        List<CourseType> list=getByParentId();
        //list集合转json字符串
        String jsonStr = JSONObject.toJSONString(list);
        //放入redis中
        redisClient.set(COURSE_TYPE, jsonStr);
    }

    @Override
    public boolean save(CourseType entity) {
        super.save(entity);
        syncOperate();
        return true;
    }



    @Override
    public boolean removeById(Serializable id) {
        super.removeById(id);
        syncOperate();
        return true;
    }

    @Override
    public boolean updateById(CourseType entity) {
        super.updateById(entity);
        syncOperate();
        return true;
    }
}
