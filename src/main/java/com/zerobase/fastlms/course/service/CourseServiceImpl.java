package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.Repository.CourseRepository;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private LocalDate getLocalDate(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
           return LocalDate.parse(value, formatter);
        }catch (Exception e){

        }

        return null;
    }

    @Override
    public boolean add(CourseInput courseInput) {

        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Course course = Course.builder()
                .categoryId(courseInput.getCategoryId())
                .subject(courseInput.getSubject())
                .regDt(LocalDateTime.now())
                .keyword(courseInput.getKeyword())
                .summary(courseInput.getSummary())
                .contents(courseInput.getContents())
                .salePrice(courseInput.getSalePrice())
                .price(courseInput.getPrice())
                .saleEndDt(saleEndDt)
                .build();

        courseRepository.save(course);

        return true;
    }

    @Override
    public CourseDto getById(long id) {
//        Optional<Course> optionalCourse = courseRepository.findById(id);
//
//        if (!optionalCourse.isPresent()) {
//            throw new RuntimeException();
//        }
//        Course course = optionalCourse.get();
//        return CourseDto.of(course);

        return  courseRepository.findById(id).map(CourseDto::of).orElseThrow(null);

    }

    @Override
    public CourseDto frontDetail(Long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isPresent()){
            return CourseDto.of(course.get());
        }
        return null;
    }

    @Override
    public List<CourseDto> frontList(CourseParam courseParam) {

        if(courseParam.getCategoryId() < 1){
            List<Course> courseList = courseRepository.findAll();
            return CourseDto.of(courseList);
        }

        Optional<List<Course>> optionalCourses = courseRepository.findByCategoryId(courseParam.getCategoryId());

        return optionalCourses.map(CourseDto::of).orElse(null);

    }

    @Override
    public boolean delete(String idList) {

        if(idList != null && idList.length() > 0){
            String[] ids= idList.split(",");


            for(String x : ids){
                Long id = 0L;
                try {
                     id = Long.parseLong(x);
                }catch (Exception e){
                    e.getStackTrace();
                }


                if(id> 0){
                    courseRepository.deleteById(id);
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(CourseInput courseInput) {
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Optional<Course> optionalCourse = courseRepository.findById(courseInput.getId());

        if(!optionalCourse.isPresent()){
            return false;
        }

        Course course = optionalCourse.get();
        course.setSubject(courseInput.getSubject());
        course.setUdtDt(LocalDateTime.now());
        course.setCategoryId(courseInput.getCategoryId());
        course.setKeyword(courseInput.getKeyword());
        course.setSummary(courseInput.getSummary());
        course.setContents(courseInput.getContents());
        course.setPrice(courseInput.getPrice());
        course.setSalePrice(courseInput.getSalePrice());
        course.setSaleEndDt(saleEndDt);

        courseRepository.save(course);


        return true;
    }

    @Override
    public List<CourseDto> list(CourseParam courseParam) {
        long totalCount = courseMapper.selectListCount(courseParam);
        List<CourseDto> list = courseMapper.selectList(courseParam);

        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for(CourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - courseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }
}
