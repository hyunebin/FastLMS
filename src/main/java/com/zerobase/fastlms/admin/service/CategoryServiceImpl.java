package com.zerobase.fastlms.admin.service;

import com.zerobase.fastlms.admin.Repository.CategoryRepository;
import com.zerobase.fastlms.admin.dto.CategoryDto;
import com.zerobase.fastlms.admin.entity.Category;
import com.zerobase.fastlms.admin.model.CateGoryInput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private Sort getSortBySortValueDesc(){
        return Sort.by(Sort.Direction.DESC, "sortValue");
    }

    @Override
    public List<CategoryDto> list() {
        List<Category> categoryList = categoryRepository.findAll(getSortBySortValueDesc());

        return CategoryDto.of(categoryList);
    }

    @Override
    public boolean add(String categoryName) {

        Category category = Category.builder()
                .categoryName(categoryName)
                .sortValue(0)
                .usingYn(true)
                .build();

        categoryRepository.save(category);

        return true;
    }

    @Override
    public boolean update(CateGoryInput cateGoryInput) {

        Optional<Category> optionalCategory = categoryRepository.findById(cateGoryInput.getId());

        if(!optionalCategory.isPresent()){
            return false;
        }


        Category category = optionalCategory.get();
        category.setCategoryName(cateGoryInput.getCategoryName());
        category.setSortValue(cateGoryInput.getSortValue());
        category.setUsingYn(category.isUsingYn());
        categoryRepository.save(category);

        return true;
    }

    @Override
    public boolean delete(Long id) {
        categoryRepository.deleteById(id);

        return true;
    }
}
