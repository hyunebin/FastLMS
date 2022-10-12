package com.zerobase.fastlms.course.mapper;

import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import org.apache.ibatis.annotations.Mapper;

import javax.persistence.MapKey;
import java.util.List;
@Mapper
public interface TakeCourseMapper {
    List<TakeCourseDto> selectList(TakeCourseParam parameter);
    List<TakeCourseDto> selectListMyCourse(TakeCourseParam parameter);
    long selectListCount(TakeCourseParam parameter);
}
