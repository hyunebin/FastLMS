package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;

import java.util.List;

public interface TakeCourseService {
    List<TakeCourseDto> list(TakeCourseParam TakeCourseParam);
    ServiceResult updateStatus(long id, String status);

    List<TakeCourseDto> myCourse(String userId);

    TakeCourseDto getCourseDetail(long id);

    ServiceResult cancelMemberCourse(long takeCourseId);
}
