package com.zerobase.fastlms.member.exception;

//에러의 직관적인 판단을 위한 exception class
public class MemberNotEmailAuthException extends RuntimeException {
    public MemberNotEmailAuthException(String s) {
        super(s);
    }
}
