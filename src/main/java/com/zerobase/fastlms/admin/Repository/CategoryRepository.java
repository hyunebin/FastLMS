package com.zerobase.fastlms.admin.Repository;

import com.zerobase.fastlms.admin.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<List<Category>> findAllBy();
}
