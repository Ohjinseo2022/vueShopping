package com.spring.study.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentsDto {
    private int idx;
    private int userIdx;
    private String title;
    private String contents;
    private String uptime;

}
