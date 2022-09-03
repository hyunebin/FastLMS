package com.zerobase.fastlms.admin.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberDto {

    private String userId;

    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDateTime;

    private boolean emailAuth;
    private String emailAuthKey;
    private LocalDateTime emailAuthTime;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt; // 해당 날짜가 지나면 사용할수없는 링크로 만들어줌
    private boolean adminYN;
}
