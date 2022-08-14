package com.zerobase.fastlms.member.service.imp;

import com.zerobase.fastlms.components.MailComponents;
import com.zerobase.fastlms.member.Repository.MemberRepository;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class MemberServiceImp implements MemberService {
    @Override
    public boolean emailAuth(String uuid) {
        Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);
        if(!optionalMember.isPresent()){
            return false;
        }

        Member member =optionalMember.get();
        member.setEmailAuth(true);
        member.setEmailAuthTime(LocalDateTime.now());
        memberRepository.save(member);

        return true;
    }

    private final MemberRepository memberRepository;
    private final MailComponents mailComponents;
    /**
    public MemberServiceImp(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
     **/

    //회원 가입
    @Override
    public boolean register(MemberInput parameter) {

        Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
        if(optionalMember.isPresent()){
            //중복된 userId가 존재한다는것
            return false;
        }
        String uuid = UUID.randomUUID().toString();

        Member member = Member.builder()
                .userId(parameter.getUserId())
                .userName(parameter.getUserName())
                .phone(parameter.getPhone())
                .password(parameter.getPassword())
                .regDateTime(LocalDateTime.now())
                .emailAuth(false)
                .emailAuthKey(uuid)
                .build();
        memberRepository.save(member);
        /** builder 패턴을 사용함으로써 위처럼 간단하게 줄일 수 있음
        Member member = new Member();
        member.setUserId(parameter.getUserId());
        member.setUserName(parameter.getUserName());
        member.setPassword(parameter.getPassword());
        member.setPhone(parameter.getPhone());
        member.setRegDateTime(LocalDateTime.now());
        member.setEmailAuth(false);
        member.setEmailAuthKey(uuid);
        **/


        String email = parameter.getUserId();
        String subject = "lms 사이트 가입 확인";
        String text = "<p>lms 사이트 가입을 축하드립니다.</p>" +
                "<p>아래 링크를 클릭하셔서 가입을 완료하세요</p>"
                +"<div><a href = http://localhost:8080/member/email-auth?id=" + uuid + "> 가입 완료 </a></div>";
        mailComponents.sendMail(email,subject,text);

        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findById(username);

        if(!optionalMember.isPresent()){
            throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
        }

        Member member = optionalMember.get();
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));



        return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
    }
}