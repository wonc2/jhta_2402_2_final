package org.example.jhta_2402_2_final.util;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Pagination {
    private int totalContent; //총 게시물 수
    private int pageScale; // 페이지당 게시물 수
    private int blockScale; // 블록당 페이지수
    private int currentPage; // 현재 페이지 번호
    private int previousPage; // 이전 페이지
    private int nextPage; // 다음 페이지
    private int totalPage; // 전체 페이지 갯수
    private int currentBlock; // 현재 페이지 블록 번호
    private int totalBlock; // 전체 페이지 블록 갯수
    private int startPage; // 현재 블록의 시작 페이지
    private int endPage; // 현재 블록의 마지막 페이지
    private int blockStart; // 페이지 블록 내에서의 시작 페이지 번호
    private int blockEnd; // 페이지 블록 내에서의 마지막 페이지 번호
    private int totalCount; // 페이지 블록 내에서의 마지막 페이지 번호
}
