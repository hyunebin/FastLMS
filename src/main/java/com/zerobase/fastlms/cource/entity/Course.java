package com.zerobase.fastlms.cource.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String imagePath;
    String keyWord;
    String subject;
    @Column(length = 1000)
    String summary;
    @Lob
    String contents;
    long price;
    long salePrice;
    LocalDateTime saleEndDt;


}
