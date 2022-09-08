package com.zerobase.fastlms.admin.controller;


import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.model.CateGoryInput;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.admin.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminCategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/category/list.do")
    public String list(Model model, MemberParam memberParam){
        List<CategoryDto> list = categoryService.list();
        model.addAttribute("list", list);

        return "/admin/category/list";
    }

    @PostMapping("/admin/category/add.do")
    public String add(Model model, CateGoryInput cateGoryInput){
        boolean result = categoryService.add(cateGoryInput.getCategoryName());

        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/admin/category/delete.do")
    public String delete(Model model, CateGoryInput cateGoryInput){
        boolean result = categoryService.delete(cateGoryInput.getId());

        return "redirect:/admin/category/list.do";
    }

    @PostMapping("/admin/category/update.do")
    public String update(Model model, CateGoryInput cateGoryInput){
        boolean result = categoryService.update(cateGoryInput);

        return "redirect:/admin/category/list.do";
    }

}
