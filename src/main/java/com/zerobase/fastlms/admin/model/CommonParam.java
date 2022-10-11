package com.zerobase.fastlms.admin.model;

import lombok.Data;

@Data
public class CommonParam {
    
    long pageIndex;
    long pageSize;
    
    String searchType; // 제목, 이름 등등 찾을 컬럼? 이라 생각하는게 좋은듯
    String searchValue;
    
    
    public long getPageStart() {
        init();
        return (pageIndex - 1) * pageSize;
    }
    
    public long getPageEnd() {
        init();
        return pageSize;
    }
    
    public void init() { // 만약 pageIndex가 1보다 작으면 1로
        if (pageIndex < 1) {
            pageIndex = 1;
        }
    
        if (pageSize < 10) {// page 크기가 10보다 작으면 10으로 고정
            pageSize = 10;
        }
    }
    
    
    public String getQueryString() {
        init();
        
        StringBuilder sb = new StringBuilder();
        
        if (searchType != null && searchType.length() > 0) {
            sb.append(String.format("searchType=%s", searchType));
        }
        
        if (searchValue != null && searchValue.length() > 0) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("searchValue=%s", searchValue));
        }
        
        return sb.toString();
    }
}
