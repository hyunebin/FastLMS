package com.zerobase.fastlms.course.controller;


import com.zerobase.fastlms.admin.service.CategoryService;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import com.zerobase.fastlms.course.service.CourseService;
import com.zerobase.fastlms.course.service.TakeCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminTakeCourseController extends BaseController {
    
    private final CourseService courseService;
    private final TakeCourseService takeCourseService;
    
    @GetMapping("/admin/takecourse/list.do")// 회원들의 수강 신청 리스트 불러옴
    public String list(Model model, TakeCourseParam parameter
        , BindingResult bindingResult) {
    
        parameter.init();
        List<TakeCourseDto> list = takeCourseService.list(parameter);// 리스트를 불러오는 로직
        
        long totalCount = 0;
        if (!CollectionUtils.isEmpty(list)) {
            totalCount = list.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);
    
        model.addAttribute("list", list);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/takecourse/list";
    }

    @PostMapping("/admin/takecourse/status.do")
    public String status(Model model, TakeCourseParam takeCourseParam){
        takeCourseParam.init();
        ServiceResult result = takeCourseService.updateStatus(takeCourseParam.getId(), takeCourseParam.getStatus()); // 회원의 수강 신청 상태 관리 로직

        if(!result.isResult()){
            model.addAttribute("message",result.getMessage());
        }

        return "redirect:/admin/takecourse/list.do";
    }

}
