package com.zerobase.fastlms.admin.dto;

import com.zerobase.fastlms.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class MemberDto {

    private String userId;

    private String userName;
    private String phone;
    private String password;
    private LocalDateTime regDateTime;
    private LocalDateTime updateDateTime;

    private boolean emailAuth;
    private String emailAuthKey;
    private LocalDateTime emailAuthTime;

    private String resetPasswordKey;
    private LocalDateTime resetPasswordLimitDt; // 해당 날짜가 지나면 사용할수없는 링크로 만들어줌
    private boolean adminYN;
    private String userStatus;

    private long totalCount;
    private long seq;

    private String zipcode;
    private String addr;
    private String addrDetail;


    public static MemberDto of(Member member) {

        return MemberDto.builder()
                .userId(member.getUserId())
                .userName(member.getUserName())
                .phone(member.getPhone())
                //.password(member.getPassword())
                .regDateTime(member.getRegDateTime())
                .updateDateTime(member.getUpdateDateTime())
                .emailAuth(member.isEmailAuth())
                .emailAuthTime(member.getEmailAuthTime())
                .emailAuthKey(member.getEmailAuthKey())
                .resetPasswordKey(member.getResetPasswordKey())
                .resetPasswordLimitDt(member.getResetPasswordLimitDt())
                .adminYN(member.isAdminYN())
                .userStatus(member.getUserStatus())
                .zipcode(member.getZipcode())
                .addr(member.getAddr())
                .addrDetail(member.getAddrDetail())
                .build();
    }

    public String getRegDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return regDateTime != null ? regDateTime.format(formatter) : "";
    }

    public String getUdtDtText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        return updateDateTime != null ? updateDateTime.format(formatter) : "";

    }

}
