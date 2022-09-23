package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.Repository.TakeCourseRepository;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.dto.TakeCourseDto;
import com.zerobase.fastlms.course.entity.TakeCourse;
import com.zerobase.fastlms.course.mapper.TakeCourseMapper;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TakeCourseServiceImpl implements TakeCourseService {

    private final TakeCourseMapper takeCourseMapper;
    private final TakeCourseRepository takeCourseRepository;
    @Override
    public ServiceResult updateStatus(long id, String status) {
        Optional<TakeCourse> optionalTakeCourse = takeCourseRepository.findById(id);
        if(!optionalTakeCourse.isPresent()){
            return new ServiceResult(false, "수강 정보가 없습니다.");
        }
        TakeCourse takeCourse = optionalTakeCourse.get();
        takeCourse.setStatus(status);
        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true,"");
    }

    @Override
    public List<TakeCourseDto> list(TakeCourseParam takeCourseParam) {
        long totalCount = takeCourseMapper.selectListCount(takeCourseParam);
        List<TakeCourseDto> list = takeCourseMapper.selectList(takeCourseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for (TakeCourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - takeCourseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }
}

