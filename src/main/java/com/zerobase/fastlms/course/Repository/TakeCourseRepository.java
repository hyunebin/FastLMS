package com.zerobase.fastlms.course.Repository;

import com.zerobase.fastlms.course.entity.TakeCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface TakeCourseRepository extends JpaRepository<TakeCourse, Long> {
    //3개의 조건을 두어 있는지 없는지 확인
    long countByCourseIdAndUserIdAndStatusIn(long courseId, String userId, Collection<String> statusList);
}
