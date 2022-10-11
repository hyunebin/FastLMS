package com.zerobase.fastlms.member.model;

import lombok.Data;

@Data
//비밀번호 초기화를 위해 사용되는 model
public class ResetPasswordInput {
    private String userId;
    private String userName;

    private String id; // 파라미터 id
    private String password;
}
