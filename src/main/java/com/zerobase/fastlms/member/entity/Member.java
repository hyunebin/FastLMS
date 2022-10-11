package com.zerobase.fastlms.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder

//member의 엔티티
public class Member implements MemberCode{
    @Id
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

    private String userStatus;

    //단순 관리자 여부를 지정 or 회원에 따른 ROLE을 지정할꺼냐
    //준회원/정회원/특별회원/관리자 등등등

    private String zipcode;
    private String addr;
    private String addrDetail;

}
