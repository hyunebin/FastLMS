package com.zerobase.fastlms.member.service.imp;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.mapper.MemberMapper;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.components.MailComponents;
import com.zerobase.fastlms.course.service.ServiceResult;
import com.zerobase.fastlms.member.Repository.MemberRepository;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.entity.MemberCode;
import com.zerobase.fastlms.member.exception.MemberNotEmailAuthException;
import com.zerobase.fastlms.member.exception.MemberStopUserException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor

public class MemberServiceImp implements MemberService {


    /**
     public MemberServiceImp(MemberRepository memberRepository){
     this.memberRepository = memberRepository;
     }
     **/
    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;

    private final MemberMapper memberMapper;

    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if(!optionalMember.isPresent()){
            return false;
        }

        Member member = optionalMember.get();
        //이미 활성화된 계정은 활성화 취소
        if(member.isEmailAuth()){
            return false;
        }

        member.setUserStatus(MemberCode.MEMBER_STATUS_ING);
        member.setEmailAuth(true);
        member.setEmailAuthTime(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean updatePassword(String userId, String password) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        //초기화 날짜 유효한지까지 체크
        Member member = optionalMember.get();

        String encPassWord = BCrypt.hashpw(password, BCrypt.gensalt());

        member.setPassword(encPassWord);
        memberRepository.save(member);

        return true;
    }

    @Override
    public ServiceResult memberUpdate(MemberInput memberInput) {
        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId());

        if(!optionalMember.isPresent()){
            return new ServiceResult(false,"회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        member.setPhone(memberInput.getPhone());
        member.setUpdateDateTime(LocalDateTime.now());

        memberRepository.save(member);
        return new ServiceResult(true,"");
    }

    @Override
    public ServiceResult memberUpdatePassword(MemberInput memberInput) {
        Optional<Member> optionalMember = memberRepository.findById(memberInput.getUserId()); // 해당 사용자의 정보를 불러옴

        if(!optionalMember.isPresent()){
            return new ServiceResult(false, "회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        if(!BCrypt.checkpw(memberInput.getPassword(), member.getPassword())){ // 암호화 된 비밀번호와 회원이 입력한 비밀번호가 같은지 확인
            return new ServiceResult(false, "비밀번호가 일치하지 않습니다.");
        }

        String encPassword = BCrypt.hashpw(memberInput.getNewPassword(), BCrypt.gensalt());// 새롭게 입력한 비밀번호
        member.setPassword(encPassword);//변경
        memberRepository.save(member);//저장


        return new ServiceResult(true,"");
    }

    @Override
    public boolean updateStatus(String userId, String userStatus) {
        Optional<Member> optionalMember = memberRepository.findById(userId);
        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        Member member = optionalMember.get();
        member.setUserStatus(userStatus);
        memberRepository.save(member);

        return true;
    }

    @Override
    public MemberDto detail(String userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);// 유저 아이디에 따른 optional 객체를 리턴

        if(!optionalMember.isPresent()){ //존재하지 않는다면
            return null;
        }

        Member member = optionalMember.get(); // 존재한다면 get() 사용해 Member객체에

        return  MemberDto.of(member); // member -> dto builder를 사용
    }

    //회원 가입
    @Override
    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId()); //null값 처리를 위한 Optional // memberRepository에서 userId를 찾음 있으면 Optional 객체를 반환
        if(optionalMember.isPresent()){ // 이미 memberRepository에 아이디가 존재한다면 ? 중복이라는것 리턴값으로 false를 넘겨줌
            return false;
        }

        String uuid = UUID.randomUUID().toString(); // 중복값을 방지하긴위한 UUID
        String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt()); // 비밀번호 암호와
        Member member = Member.builder() // parameter에서 넘어온 값들 + 생성한 값들로 Member객체를 Builder() 패턴을 사용해서 생성
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(encPassword)
                .regDateTime(LocalDateTime.now())
                .emailAuth(false)
                .emailAuthKey(uuid)
                .userStatus(Member.MEMBER_STAUTS_REQ)
                .build();
        memberRepository.save(member); // DB에 저장

        String email = parameter.getUserId();
        String subject = "lms 사이트 가입 확인";
        String text = "<p>lms 사이트 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요</p>"
                +"<div><a href = http://localhost:8080/member/email-auth?id=" + uuid + "> 가입 완료 </a></div>";
        mailComponents.sendMail(email,subject,text); // 인증 이메일을 보냄

        return true;
    }

    @Override
    public boolean checkResetPassword(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(uuid);// 해당 키를 가진 유저를 찾음
        if(!optionalMember.isPresent()){//유저가 없다면
            return false;
        }

        //초기화 날짜 유효한지까지 체크
        Member member = optionalMember.get();

        if(member.getResetPasswordLimitDt() == null ){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유요한 날짜가 아닙니다.");
        }
        return true;
    }

    @Override
    public boolean resetPassword(String id, String password) {
        Optional<Member> optionalMember = memberRepository.findByResetPasswordKey(id); // 비밀번호를 바꿀 유저를 판단하기위한 uuid
        if(!optionalMember.isPresent()){ //해당 유저가 없다면
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }


        Member member = optionalMember.get();

        //초기화 날짜 유효한지까지 체크
        if(member.getResetPasswordLimitDt() == null){
            throw new RuntimeException("유효한 날짜가 아닙니다.");
        }

        if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("유요한 날짜가 아닙니다.");
        }
        //새로운 비밀번호 설정
        String encPassword = BCrypt.hashpw(password, BCrypt.gensalt()) ;
        member.setPassword(encPassword);
        member.setResetPasswordLimitDt(null);
        member.setResetPasswordKey("");//비밀번호를 바꾼유저는 비밀번호를 바꿀 유저를 판단하는 key값을 초기화
        memberRepository.save(member);

        return true;
    }

    @Override
    public boolean sendResetPassword(ResetPasswordInput resetPasswordInput) {
        Optional<Member> optionalMember = memberRepository.findByUserIdAndUserName(resetPasswordInput.getUserId(), resetPasswordInput.getUserName()); // user의 이름과 아이디로 회원의 정보를 조회

        if(!optionalMember.isPresent()){ //회원 정보가 없다면
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }
        //회원 정보가 있다면
        Member member = optionalMember.get();


        String uuid = UUID.randomUUID().toString();
        member.setResetPasswordKey(uuid); // 비밀번호 변경시 보내는 이메일과 동일한 유저인지 확인하기 위한 uuid
        member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1)); // 하루정도까지
        memberRepository.save(member);

        String email = resetPasswordInput.getUserId();
        String subject = "lms 비밀번호 초기화 메일 입니다.";
        String text = "<p>lms 비밀번호 초기화 메일 입니다.</p>" +
                "<p>아래 링크를 클릭하셔서 비밀번호를 초기화 해주세요</p>"
                +"<div><a href = http://localhost:8080/member/reset/password?id=" + uuid + "> 초기화 링크 </a></div>";
        mailComponents.sendMail(email,subject,text);


        return true;
    }

    @Override
    public List<MemberDto> list(MemberParam memberParam) {
        long totalCount = memberMapper.selectListCount(memberParam);
        List<MemberDto> list = memberMapper.selectList(memberParam);
        if (!CollectionUtils.isEmpty(list)) {
            int i = 0;
            for(MemberDto x : list) {
                x.setTotalCount(totalCount);
                x.setSeq(totalCount - memberParam.getPageStart() - i);
                i++;
            }
        }

        return list;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();


        if(Member.MEMBER_STAUTS_REQ.equals(member.getUserStatus())){
            throw new MemberNotEmailAuthException("이메일을 인증을 해주세요");
        }

        if(Member.MEMBER_STATUS_STOP.equals(member.getUserStatus())){
            throw new MemberStopUserException("정지된 회원 입니다.");
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));


        if(member.isAdminYN()){
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}
