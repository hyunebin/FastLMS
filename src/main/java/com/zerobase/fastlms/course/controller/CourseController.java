package com.zerobase.fastlms.course.controller;


import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CategoryService categoryService;

    @GetMapping("/course")
    public String course(Model model, CourseParam courseParam){

        List<CourseDto> list = courseService.frontList(courseParam); // 강좌 목록중 사용하는 목록 안하는 목록 존재 사용자에 보여줄 frontList가 필요
        List<CategoryDto> categoryList = categoryService.frontList(CategoryDto.builder().build());// 카테고리또한 위와 같은 이유
        int courseTotalCount = 0;
        if (categoryList != null){
            for(CategoryDto x : categoryList){
                courseTotalCount += x.getCourseCount();
            }
        }
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("list", list);
        model.addAttribute("courseTotalCount", courseTotalCount);

        return "course/index";
    }


    @GetMapping("/course/{id}")
    public String courseDetail(CourseParam courseParam, Model model){
         CourseDto courseDto = courseService.frontDetail(courseParam.getId());

         model.addAttribute("detail", courseDto);
        return "course/detail";
    }


}
