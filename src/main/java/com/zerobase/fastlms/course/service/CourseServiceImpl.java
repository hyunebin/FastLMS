package com.zerobase.fastlms.course.service;

import com.zerobase.fastlms.course.Repository.TakeCourseRepository;
import com.zerobase.fastlms.course.dto.CourseDto;
import com.zerobase.fastlms.course.Repository.CourseRepository;
import com.zerobase.fastlms.course.entity.Course;
import com.zerobase.fastlms.course.entity.TakeCourse;
import com.zerobase.fastlms.course.mapper.CourseMapper;
import com.zerobase.fastlms.course.model.CourseInput;
import com.zerobase.fastlms.course.model.CourseParam;
import com.zerobase.fastlms.course.model.ServiceResult;
import com.zerobase.fastlms.course.model.TakeCourseInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    private final TakeCourseRepository takeCourseRepository;

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

        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText()); // 종료일 설정

        //입력된 값에 따라 빌더 패턴으로 저장
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
    public List<CourseDto> listAll() {
        List<Course> courseList = courseRepository.findAll();
        return CourseDto.of(courseList);
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
    public ServiceResult req(TakeCourseInput takeCourseInput) {
        ServiceResult result = new ServiceResult();
        Optional<Course> optionalCourse = courseRepository.findById(takeCourseInput.getCourseId());//수강신청한 강좌를 가져옴

        if(!optionalCourse.isPresent()){//해당 강좌 정보가 없다면
            return new ServiceResult(false, "강좌 정보가 존재하지 않습니다.");
        }

        Course course = optionalCourse.get();//코스 정보를 가져오고

        //이미 신청한 강좌인지 확인
        String[] statusList = {TakeCourse.STATUS_CANCEL, TakeCourse.STATUS_COMPLETE, TakeCourse.STATUS_REQ};
        long count = takeCourseRepository.countByCourseIdAndUserIdAndStatusIn(course.getId(),takeCourseInput.getUserId(), Arrays.asList(statusList));

        if(count > 0 ){
            return new ServiceResult(false, "이미 신청한 강좌입니다.");
        }

        TakeCourse takeCourse = TakeCourse.builder() // 수강정보와 유저정보를 합쳐서 수강신청 객체를 만들어줌
                .courseId(course.getId())
                .userId(takeCourseInput.getUserId())
                .payPrice(course.getSalePrice())
                .regDt(LocalDateTime.now())
                .status(TakeCourse.STATUS_REQ)
                .build();

        takeCourseRepository.save(takeCourse);

        return new ServiceResult(true,"수강신청에 성공하였습니다.");
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
            String[] ids= idList.split(","); // idList는 강좌의 번호를 가지고 있음(ex -> 1,2,3,5,6) 그것은 하나씩 나눠줌


            for(String x : ids){
                Long id = 0L;
                try {
                     id = Long.parseLong(x);
                }catch (Exception e){
                    e.getStackTrace();
                }


                if(id> 0){ // 삭제할 아이디가 있으면
                    courseRepository.deleteById(id);//db에서 제거
                }
            }
        }

        return true;
    }

    @Override
    public boolean update(CourseInput courseInput) {// 강좌정보 업데이트
        LocalDate saleEndDt = getLocalDate(courseInput.getSaleEndDtText());

        Optional<Course> optionalCourse = courseRepository.findById(courseInput.getId());

        if(!optionalCourse.isPresent()){
            return false;
        }


        //수정할 데이터
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
        long totalCount = courseMapper.selectListCount(courseParam);//java는 myBatis를 Mapper라는것을 사용해 사용 -> 내용은 course table의 컬럼의 갯수를 리턴해줌
        List<CourseDto> list = courseMapper.selectList(courseParam);// 등록일에 따라 course table의 내용을 리턴해준다.

        if (!CollectionUtils.isEmpty(list)) {// 리스트의 행에 대해서 번호를 매기기 위해서 사용
            int i = 0;// 0으로 시작
            for(CourseDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - courseParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }
}
