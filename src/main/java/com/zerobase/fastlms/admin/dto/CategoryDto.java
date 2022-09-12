package com.zerobase.fastlms.admin.dto;


import com.zerobase.fastlms.admin.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto {
    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;

    public static List<CategoryDto> of(List<Category> categoryList){
        if(categoryList != null){
            List<CategoryDto> categoryDtoList = new ArrayList<>();

            for(Category x : categoryList){
                categoryDtoList.add(of(x));
            }
            return categoryDtoList;
        }

        return null;
    }

    public static CategoryDto of(Category category){
        return  CategoryDto.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .sortValue(category.getSortValue())
                .usingYn(category.isUsingYn())
                .build();
    }
}
