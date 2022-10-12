package com.zerobase.fastlms.member.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
//맴버등록시 사용할 model
public class MemberInput {
    private String userId;
    private String userName;
    private String password;
    private String phone;
    private String newPassword;
    private String zipcode;
    private String addr;
    private String addrDetail;
}
