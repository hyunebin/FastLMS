package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.model.ServiceResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RequiredArgsConstructor
@RestController

public class ApiCourseController extends BaseController{

    private final CourseService courseService;
    private final CategoryService categoryService;

    @PostMapping("/api/course/req.api")
    public ResponseEntity<?> courseReq(
            @RequestBody TakeCourseInput takeCourseInput,
            Principal principal)
    {
        takeCourseInput.setUserId(principal.getName());
        ServiceResult result = courseService.req(takeCourseInput); //신청결과를 리턴해줌

        if(!result.isResult()){ // 신청결과가 false라면
            ResponseResult responseResult = new ResponseResult(false, result.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);//성공시

        return ResponseEntity.ok().body(responseResult);
    }

}
