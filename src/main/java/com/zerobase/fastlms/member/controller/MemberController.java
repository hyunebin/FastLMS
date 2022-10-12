package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.service.ServiceResult;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService; // 생성자 주입을 위한 private final
    private final TakeCourseService takeCourseService;// 맴버가 자신에 대한 수강신청 관리

    @RequestMapping("/member/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/member/register")
    public String register() {
        return "member/register";
    }

    @PostMapping("/member/register")//회원 등록
    public String registerSubmit(Model model, MemberInput parameter) {
        //Model -> Client 에게 데이터를 내리기위해서 사용
        boolean result = memberService.register(parameter);
        model.addAttribute("result", result); // 결과를 모델에 추가 view에서 사용

        return "member/register_complete";
    }

    @GetMapping("/member/info")//회원 상세 정보
    public String memberInfo(Principal principal, Model model) {

        String userId = principal.getName(); //Principal 객체에는 user정보가 담겨져 있음으로 얻어올 수 있다.

        MemberDto memberDto = memberService.detail(userId);//서비스 사용 user의 정보를 가져옴

        model.addAttribute("detail", memberDto);// 해당 데이터를 view에 전달

        return "member/info";
    }

    //회원 정보의 수정 - 변경 정보, 보여주는 정보, 보여줄 필요가없는 정보
    @PostMapping("/member/info")
    public String submitMemberInfo(MemberInput memberInput, Principal principal, Model model){
        memberInput.setUserId(principal.getName());
        ServiceResult result = memberService.memberUpdate(memberInput);

        if(!result.isResult()){
            model.addAttribute("message", result.getMessage());
            return "/common/error";
        }
        return "redirect:/member/info";
    }

    @GetMapping("/member/find/password")
    public String findPassword() {
        return "member/find_password";
    }

    @PostMapping("/member/find/password") // 비밀번호 찾기
    public String findPasswordSubmit(Model model, ResetPasswordInput resetPasswordInput) {
        boolean result = false;

        try {
            result = memberService.sendResetPassword(resetPasswordInput);
        } catch (Exception e) {

        }

        model.addAttribute("result", result);

        return "member/find_password_result";
    }

    //쿼리파라미터 받기
    @GetMapping("/member/email-auth")
    public String emailAuth(Model model, HttpServletRequest request) {
        String uuid = request.getParameter("id"); //id = serviceimp의 url에서 적용한 이름
        boolean result = memberService.emailAuth(uuid);

        model.addAttribute("result", result);
        return "member/email-auth";
    }

    @GetMapping("/member/reset/password")
    public String resetPassword(HttpServletRequest request, Model model) {
        String uuid = request.getParameter("id"); // 비빌번호 초기화 메일을 받을시 설정한 uuid
        model.addAttribute("uuid", uuid);

        boolean result = memberService.checkResetPassword(uuid);
        model.addAttribute("result", result);

        return "member/reset_password";
    }

    @PostMapping("/member/reset/password")// 변경할 비빌번호를 입력하는 페이지
    public String resetPasswordSubmit(Model model, ResetPasswordInput resetPasswordInput) {
        boolean result = false;

        try {
            result = memberService.resetPassword(resetPasswordInput.getId(), resetPasswordInput.getPassword());// 비밀번호 변경을 위한 로직
        } catch (Exception e) {

        }

        model.addAttribute("parameter", resetPasswordInput); // view에 데이터 넘김
        model.addAttribute("result", result);// view에 데이터 넘김

        return "member/reset_password_result";
    }

    @GetMapping("/member/password")
    public String memberPassword() {
        return "member/password";
    }


    @PostMapping("/member/password")
    public String SubmitMemberPassword(MemberInput memberInput, Principal principal, Model model) {
        String userId = principal.getName(); // 접속한 회원의 아이디
        memberInput.setUserId(userId);// 해당 회원의 아이디로 정보를 받아오기 위해

        ServiceResult result = memberService.memberUpdatePassword(memberInput); // 비밀번호 변경 로직

       if(!result.isResult()){
           model.addAttribute("message", result.getMessage());
           return "common/error";
       }

        return "redirect:/member/info";
    }

    @GetMapping("/member/takecourse")
    public String takeCourse(Model model, Principal principal) {
        String userId = principal.getName();
        List<TakeCourseDto> myCourse = takeCourseService.myCourse(userId);

        model.addAttribute("list", myCourse);
        return "member/takecourse";
    }

    @PostMapping("/member/takecourse")
    public String cancleTakeCourse(Model model, Principal principal) {

        return "member/takecourse";
    }


}
