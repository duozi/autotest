package com.xn.front.interaction.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Pagenation<T> extends ArrayList<T> implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * 每页显示行数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private int totalRecord;

    /**
     * 总页数
     */
    private int totalPage;

    /**
     * 当前页数
     */
    private int currentPage;
    
    /**存放结果集*/
    private List<T> result = new ArrayList<T>();
    
    public Pagenation() {
        this.pageSize = 10;
    }
     
    public int getCurrentResult() {
        if (currentPage == 1) {
            return 0;
        } else if (currentPage > 1) {
            return (currentPage - 1) * pageSize;
        }
        return 0;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    /**
     * @return 当前页
     */
    public int getCurrentPage() {
        return currentPage;
    }
    
    /**
     * 设置当前页
     * @param currentPage
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }
    
    /**
     * @return 每页显示数
     */
    public int getPageSize() {
        return pageSize;
    }
    
    /**
     * 设置每页显示数
     * @param pageSize
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    
    /**
     * @return 总页数
     */
    public int getTotalPage() {
        return totalPage;
    }
    
    /**
     * 总页数
     * @param totalPage
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    /**
     * 返回总记录数
     * @return
     */
    public int getTotalRecord() {
        return totalRecord;
    }
    
    /**
     * 总记录数
     * @param totalRecord
     */
    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        if (this.pageSize != 0 && this.totalRecord != 0) {
            if (totalRecord % this.pageSize > 0) {
                this.totalPage = totalRecord / this.pageSize + 1;
            } else {
                this.totalPage = totalRecord / this.pageSize;
            }
        }
        if((this.currentPage-1)*this.pageSize+this.pageSize>this.totalRecord){
            this.currentPage=this.totalPage;
        }
    }

    /**
     * 返回下一页数据的起始行
     * @return
     */
    public int getOffset(){
        int offset = (this.currentPage - 1) * this.pageSize;
        if (offset < 0) {
            offset = 0;
        }
        return offset;
    }

    /**
     * 返回下一页取多少行数据
     * @return
     */
    public int getMaxRow(){
        return this.pageSize;
    }


}
