package com.zerobase.fastlms.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor

public class ServiceResult {
    boolean result;
    String message;
}
