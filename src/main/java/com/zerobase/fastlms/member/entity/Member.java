package com.zerobase.fastlms.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.bytebuddy.asm.Advice;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder

public class Member {
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

}
