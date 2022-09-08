package com.zerobase.fastlms.admin.model;

import lombok.Data;

@Data
public class CateGoryInput {
    Long id;
    String categoryName;
    int sortValue;
    boolean usingYn;
}
