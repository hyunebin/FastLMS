package com.zerobase.fastlms.cource.controller;

import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.cource.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class adminCourseController {

    private final CourseService courseService;

    @GetMapping("/admin/course/list.do")
    public String list(Model model, MemberParam memberParam){

        return "/admin/course/list";
    }
}
