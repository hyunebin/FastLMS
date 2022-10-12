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

    private final CourseService courseService; // 서비스를 사용하기 위한 생성자 주입
    private final CategoryService categoryService;// 서비스를 사용하기 위한 생성자 주입

    @GetMapping("/admin/course/list.do")
    public String list(Model model, CourseParam parameter) {

        parameter.init();
        List<CourseDto> courses = courseService.list(parameter); // 코스의 리스트를 불러오는 로직

        long totalCount = 0;
        if (courses != null && courses.size() > 0) {
            totalCount = courses.get(0).getTotalCount();
        }

        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString); // 조건문에 따른 처리 또한 포함

        model.addAttribute("list", courses);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/course/list";
    }


    @GetMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})//2개의 페이지는 거의 같은 역할을 함
    public String add(Model model, HttpServletRequest httpServletRequest, CourseParam courseParam){

        boolean editMode = httpServletRequest.getRequestURI().contains("/edit.do");// 2개의 페이지의 uri를 공유함으로 수정일때는 uri에 포함된 단어로 구분
        CourseDto detail = new CourseDto();


        if(editMode){//수정모드라면
            long id = courseParam.getId(); // 강좌번호를 가져옴

            CourseDto courseDto = courseService.getById(id);// 강좌 번호를 통해 해당 강좌의 정보를 가져옴

            if(courseDto == null){//없을 경우 처리
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
    public String deleteSubmit(CourseInput courseInput){

        boolean result = courseService.delete(courseInput.getIdList()); //강좌 삭제는 선택삭제와 전체 삭제가 있음 이것을 문자열에 담아서 String[] 변경해 사용하기 대문에 IdList 사용

        return "redirect:/admin/course/list.do";
    }

    @PostMapping(value = {"/admin/course/add.do", "/admin/course/edit.do"})
    public String addSubmit(Model model, CourseInput courseInput, HttpServletRequest httpServletRequest){

        boolean editMode = httpServletRequest.getRequestURI().contains("/edit.do"); // 위와 동일하게 수정인지 추가인지 확인하기 위해

        if(editMode){ //만약 수정이라면
            long id = courseInput.getId();

            CourseDto courseDto = courseService.getById(id);

            if(courseDto == null){
                model.addAttribute("message", "강좌정보가 존재하지 않습니다.");
                return "common/error";
            }

            boolean result = courseService.update(courseInput); // 업데이트 로직 사용

        }else{
            boolean result = courseService.add(courseInput); // 추가 로직을 사용
        }



        return "redirect:/admin/course/list.do";
    }
}
