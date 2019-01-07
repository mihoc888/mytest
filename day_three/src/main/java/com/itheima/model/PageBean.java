package com.itheima.model;

import java.util.List;

/**
 * 每个PageBean封装1页的数据
 */
public class PageBean<T> {
    private List<T> dataList;               //（业务层封装）当前页数据列表,从数据库获取，在业务层封装
    private int firstPage=1;                //首页,直接设置为1
    private int prePage;                    //上一页，当前封装=curPage-1
    private int curPage;                    //（业务层封装）当前页,从前端传递过来，指明用户要查询哪一页的数据，在业务层封装。
    private int nextPage;                   //下一页，当前封装=curPage+1
    private int totalPage;                  //总页数（末页）,由总条数和每页大小绝对，当前封装
    private int count;                      //（业务层封装）总条数,从数据库获取，在业务层封装
    private int pageSize;                   //（业务层封装）每页大小，就是页面显示几条数据

    public PageBean() {
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public int getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(int firstPage) {
        this.firstPage = firstPage;
    }

    public int getPrePage() {
        if(curPage>1) {
            prePage = curPage - 1;
        }
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getNextPage() {
        if(curPage<getTotalPage()) {
            nextPage = curPage + 1;
        }
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getTotalPage() {
        totalPage=count%pageSize==0?count/pageSize:count/pageSize+1;
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
