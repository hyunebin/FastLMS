package com.zerobase.fastlms.admin.controller;

import com.zerobase.fastlms.course.model.CourseParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class AdminTakeCourseController {

    @GetMapping("/admin/takeCourse/list.do")
    public String list(Model model, CourseParam courseParam){
        courseParam.init();



        return "admin/takecourse/list";
    }
}
