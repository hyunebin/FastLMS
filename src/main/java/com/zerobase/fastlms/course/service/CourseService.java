package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;

import java.util.List;

public interface CourseService {
    boolean add(CourseInput courseInput);

    List<CourseDto> list(CourseParam courseParam); // 코스 리스트 불러옴

    CourseDto getById(long id);

    boolean update(CourseInput courseInput);

    boolean delete(String idList);

    List<CourseDto> frontList(CourseParam courseParam);

    CourseDto frontDetail(Long id);

    ServiceResult req(TakeCourseInput takeCourseInput);

    List<CourseDto> listAll();
}
