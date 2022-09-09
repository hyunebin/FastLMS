package com.zerobase.fastlms.cource.Repository;

import com.zerobase.fastlms.cource.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
