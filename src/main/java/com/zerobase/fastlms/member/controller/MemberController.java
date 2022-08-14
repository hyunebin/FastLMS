package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.member.service.MemberService;
import com.zerobase.fastlms.member.model.MemberInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

   /** RequiredArgsConstructor 어노테이션이 생성자를 대신 생성해줌
   public MemberController(MemberService memberService){
        this.memberService = memberService;
    }**/

   @RequestMapping("/member/login")
   public String login(){
       return "member/login";
   }



    @GetMapping("/member/register")
    public String register(){
        return "member/register";
    }

    @PostMapping("/member/register")
    public String registerSubmit(Model model, HttpServletRequest request, HttpServletResponse response, MemberInput parameter){
        //Model -> client한테 데이터를 내리기위해서 사용
        boolean result = memberService.register(parameter);
        model.addAttribute("result", result);

        return "member/register_complete";
    }

    @GetMapping("member/info")
    public String memberInfo(){
        return "member/info";
    }

    //쿼리파라미터 받기
    @GetMapping("member/email-auth" )
    public String emailAuth(Model model, HttpServletRequest request){
        String uuid = request.getParameter("id"); //id = serviceimp의 url에서 적용한 이름
        boolean result =  memberService.emailAuth(uuid);

        model.addAttribute("result", result);
     return "member/email-auth";
    }
}
