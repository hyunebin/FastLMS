package com.zerobase.fastlms.main.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



//주소를 매핑하기 위해서 만든 클래스
// 논리적 주소와 물리적 주소를 매핑해주기 위해서!!


//RestController 와 Controller의 차이 정리 필요

@RequiredArgsConstructor
@Controller
public class MainController {
    @RequestMapping("/")
    public String index(){
        return "index";
    }

    //request 객체 -> WEB -> Server
    //response 객체 Server -> WEB
    //Spring MVC -> View가 템플릿 엔진 화면에 내용을 출력해줌

    @RequestMapping("/error/denied")
    public String errorDenied(){
        return "error/denied";
    }


}
