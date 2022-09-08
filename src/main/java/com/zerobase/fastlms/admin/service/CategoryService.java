package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.entity.Category;
import com.zerobase.fastlms.admin.model.CateGoryInput;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> list();

    boolean add(String categoryName);

    boolean update(CateGoryInput cateGoryInput);

    boolean delete(Long id);
}
