package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.course.service.ServiceResult;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput params);// 맴버 등록
    boolean emailAuth(String uuid); // uuid에 해당하는 계정을 활성화
    boolean sendResetPassword(ResetPasswordInput resetPasswordInput); //입력한 이메일로 비밀번호 초기화 정보를 전송하는 메소드
    boolean resetPassword(String id, String password);// 입력받은 uuid를 통해 패스워드를 초기화함
    boolean checkResetPassword(String uuid);// 실제 변경할 비빌번호를 입력해 처리
    List<MemberDto> list(MemberParam memberParam); //회원 목록을 리턴(관리자에서만 사용  )

    MemberDto detail(String userId); // 유저의 정보를 받아옴

    boolean updateStatus(String userId, String userStatus1);

    boolean updatePassword(String userId, String password);

    ServiceResult memberUpdatePassword(MemberInput memberInput); // 사용자의 비밀번호 변경

    ServiceResult memberUpdate(MemberInput memberInput);

    ServiceResult withdraw(String name, String password);
}
