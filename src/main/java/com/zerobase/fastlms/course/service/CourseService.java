package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;

import java.util.List;

public interface CourseService {
    boolean add(CourseInput courseInput);

    List<CourseDto> list(CourseParam courseParam);

    CourseDto getById(long id);

    boolean update(CourseInput courseInput);

    boolean delete(String idList);

    List<CourseDto> frontList(CourseParam courseParam);
}
