package com.zerobase.fastlms.member.entity;

public interface MemberCode {// 유저의 상태를 판단하기 위한 값들
    // 이용 가능
    String MEMBER_STATUS_ING = "ING";

    // 이용 중지
    String MEMBER_STATUS_STOP = "STOP";

    //맴버 가입 대기중
    String MEMBER_STAUTS_REQ = "REQ";
}
