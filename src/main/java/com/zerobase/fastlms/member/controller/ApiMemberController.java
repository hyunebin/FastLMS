package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.common.model.ResponseResult;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import com.zerobase.fastlms.course.service.TakeCourseService;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
public class ApiMemberController {

    private final MemberService memberService;
    private final TakeCourseService takeCourseService;

    @PostMapping("/api/member/course/cancel.api")
    public ResponseEntity<?> courseCancelReq(@RequestBody TakeCourseInput takeCourseInput, Model model, Principal principal){

        //나의 강좌인지 확인을 해야함
        TakeCourseDto takeCourseDto = takeCourseService.getCourseDetail(takeCourseInput.getTakeCourseId());

        if(takeCourseDto == null){
            ResponseResult responseResult = new ResponseResult(false, "수강 신청 정보가 존재하지 않음");
            return ResponseEntity.ok().body(responseResult);
        }

        //내가 신청한 강좌가 맞는지 확인
        if(principal.getName() == null || !principal.getName().equals(takeCourseDto.getUserName())){
            ResponseResult responseResult = new ResponseResult(false, "내 수강 신청 정보가 아닙니다.");
            return ResponseEntity.ok().body(responseResult);
        }

        ServiceResult serviceResult = takeCourseService.cancelMemberCourse(takeCourseInput.getTakeCourseId());

        if(!serviceResult.isResult()){
            ResponseResult responseResult = new ResponseResult(false, serviceResult.getMessage());
            return ResponseEntity.ok().body(responseResult);
        }

        ResponseResult responseResult = new ResponseResult(true);//성공시
        return ResponseEntity.ok().body(responseResult);
    }

}
