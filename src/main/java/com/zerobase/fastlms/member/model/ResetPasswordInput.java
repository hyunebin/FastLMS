package com.zerobase.fastlms.member.model;

import lombok.Data;

@Data
public class ResetPasswordInput {
    private String userId;
    private String userName;

    private String id; // 파라미터 id
    private String password;
}
