package com.zerobase.fastlms.course.controller;

import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminCourseController extends BaseController {

    private final CourseService courseService;
    private final CategoryService categoryService;




    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam parameter) {

        parameter.init();
        List<CourseDto> courses = courseService.list(parameter);

        long totalCount = 0;
        if (courses != null && courses.size() > 0) {
            totalCount = courses.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", courses);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }


    @GetMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String add(Model model, HttpServletRequest httpServletRequest, CourseParam courseParam){

        boolean editMode = httpServletRequest.getRequestURI().contains("/edit.do");
        CourseDto detail = new CourseDto();


        if(editMode){
            long id = courseParam.getId();

            CourseDto courseDto = courseService.getById(id);

            if(courseDto == null){
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }

            detail = courseDto;
        }

        model.addAttribute("detail", detail);
        model.addAttribute("editMode", editMode);
        model.addAttribute("category", categoryService.list());
        return "/admin/course/add";
    }

    @PostMapping(value = {"/admin/course/delete.do"})
    public String deleteSubmit(Model model, CourseInput courseInput){

        boolean result = courseService.delete(courseInput.getIdList());

        return "redirect:/admin/course/list.do";
    }

    @PostMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model, CourseInput courseInput, HttpServletRequest httpServletRequest){

        boolean editMode = httpServletRequest.getRequestURI().contains("/edit.do");

        if(editMode){
            long id = courseInput.getId();

            CourseDto courseDto = courseService.getById(id);

            if(courseDto == null){
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = courseService.update(courseInput);

        }else{
            boolean result = courseService.add(courseInput);
        }



        return "redirect:/admin/course/list.do";
    }
}
