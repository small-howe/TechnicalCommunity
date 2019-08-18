package com.tangwh.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageinationDTO<T> {
    private List<T> data; // 页面上的所有属性
    private boolean showPrevious;//上一页
    private boolean showNext;// 下一页
    private boolean showFirstPage; // 回第一页 的 上一页按钮
    private boolean showEndPage;   // 回最后一页的下一页按钮


    private Integer page; //当前页
    private List<Integer> pages = new ArrayList<>(); // 下面展示的数
    private Integer totalPage;// 页码

    public void setPagination(Integer totalPage, Integer page) {
          this.totalPage=totalPage;
        this.page=page;
        // 当页面选择跳转值-1页面 让他等于1
        // 遍历 显示要展示多少页码
        pages.add(page);
        for (int i = 1; i <= 3; i++) {
            if (page - i > 0) {
                pages.add(0, page - i);
            }

            if (page + i <= totalPage) {
                pages.add(page + i);
            }
        }
            // 当点击第一页的时候 没有上一页
            if (page == 1) {
                showPrevious = false;
            } else {
                showPrevious = true;
            }

            // 是否展示下一页
            if (page == totalPage) {

                showNext = false;
            } else {
                showNext = true;
            }

            //是否展示  回到第一页按钮
            if (pages.contains(1)) {
                showFirstPage = false;
            } else {
                showFirstPage = true;

            }
            //  pages  你，固定每次展示的几页的数数据 是否展示 回到最后一页按钮
            if (pages.contains(totalPage)) {
                showEndPage = false;
            } else {
                showEndPage = true;
            }
        }
    }
