package com.majiang.community.dto;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    //话题
    private List<T> data;

    //是否有上一页
    private boolean showPrevious;

    //是否有首页
    private boolean showFirstPage;

    //是否有下一页
    private boolean showNext;

    //是否有尾页
    private boolean showEndPage;

    //当前页面
    private Integer page;

    private Integer totalPage;

    //当前展示的页面集合
    private List<Integer> pages=new ArrayList<>();

    public void setPagination(Integer totalPage, Integer page) {
        this.totalPage=totalPage;
        this.page=page;
        pages.add(page);

        for (int i=1;i<=3;i++){
            if (page-i>0){
                pages.add(0,page-i);
            }
            if (page+i<=totalPage){
                pages.add(page+i);
            }
        }

        //是否展示上一页
        if (page==1){
            showPrevious=false;
        }else {
            showPrevious=true;
        }

        //是否展示下一页
        if (page==totalPage){
            showNext=false;
        }else {
            showNext=true;
        }

        //是否展示首页
        if (pages.contains(1)){
            showFirstPage=false;
        }else{
            showFirstPage=true;
        }

        //是否展示尾页
        if(pages.contains(totalPage)){
            showEndPage=false;
        }else{
            showEndPage=true;
        }


    }
}
