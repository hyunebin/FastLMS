package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.admin.model.MemberParam;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface MemberService extends UserDetailsService {
    boolean register(MemberInput parm);
    boolean emailAuth(String uuid); // uuid에 해당하는 계정을 활성화
    boolean sendResetPassword(ResetPasswordInput resetPasswordInput); //입력한 이메일로 비밀번호 초기화 정보를 전송하는 메소드
    boolean resetPassword(String id, String password);// 입력받은 uuid를 통해 패스워드를 초기화함
    boolean checkResetPassword(String uuid);
    List<MemberDto> list(MemberParam memberParam); //회원 목록을 리턴(관리자에서만 사용  )

    MemberDto detail(String userId);

    boolean updateStatus(String userId, String userStatus1);

    boolean updatePassword(String userId, String password);
}
