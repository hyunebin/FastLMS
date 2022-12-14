package com.zerobase.fastlms.admin.controller;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberInput;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor

public class AdminMemberController extends BaseController {
    private final MemberService memberService;

    @PostMapping("/admin/member/status.do")
    public String status(Model model, MemberInput memberInput){

        boolean result = memberService.updateStatus(memberInput.getUserId(), memberInput.getUserStatus());


        return "redirect:/admin/member/detail.do?userId=" + memberInput.getUserId();

    }

    @PostMapping("admin/member/password.do")
    public String passwordReset(MemberInput memberInput){
        boolean result = memberService.updatePassword(memberInput.getUserId(), memberInput.getPassword());

        return "redirect:/admin/member/detail.do?userId=" + memberInput.getUserId();
    }

    @GetMapping("/admin/member/detail.do")
    public String detail(Model model, MemberParam parameter) {
        parameter.init();

        MemberDto member = memberService.detail(parameter.getUserId());
        model.addAttribute("member", member);

        return "admin/member/detail";
    }

    @GetMapping("/admin/member/list.do")
    public String list(Model model, MemberParam parameter) {

        parameter.init();
        List<MemberDto> members = memberService.list(parameter);

        long totalCount = 0;

        if (members != null && members.size() > 0) {
            totalCount = members.get(0).getTotalCount();
        }
        String queryString = parameter.getQueryString();
        String pagerHtml = getPaperHtml(totalCount, parameter.getPageSize(), parameter.getPageIndex(), queryString);

        model.addAttribute("list", members);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pager", pagerHtml);

        return "admin/member/list";
    }
}
