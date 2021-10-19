package com.project.group.vo;



import lombok.Data;

import java.util.List;

/**
 * 分页工具类
 */
@Data
public class Page<T> {
    //当前页，前台传递，默认第一页
    private Integer pageNum;
    //每页显示几个数据
    private Integer pageSize;
    //总记录数，由后台数据库得
    private long totalCount;
    //总页数(总记录/每页显示数目)，需转浮点型，向上取整
    private Integer totalPages;
    private Integer prePage;//上一页，当前页-1，<1则为1
    private Integer nextPage;//下一页，当前页+1，>1则为总页数

    //快速定位页数
    //快速定位开始页，当前页-2，若<1则开始页为1，结束页+2，若结束页>总页数，则结束页为总页数
    private Integer startNavPage;

    private Integer endNavPage;//快速定位结束页
    private List<T> dataList;//当前页数据集合，查询数据库指定页数

    public Page(Integer pageNum, Integer pageSize, long totalCount) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalCount = totalCount;

        //总页数
        this.totalPages = (int)Math.ceil(totalCount/(pageSize*1.0));
        //上一页
        this.prePage = pageNum - 1 < 1 ? 1:pageNum-1;
        //下一页
        this.nextPage = pageNum+1 >totalPages ? totalPages:pageNum+1;

        //快速定位
        this.startNavPage = pageNum-2;//开始页默认-5
        this.endNavPage = pageNum+2;//结束页默认+4
        if (this.startNavPage<1){
            this.startNavPage=1;
            this.endNavPage = this.endNavPage+2>totalPages ? totalPages:this.endNavPage+2;
        }
        if(this.endNavPage>totalPages){
            this.endNavPage=totalPages;
            this.startNavPage = this.startNavPage-2< 1 ? 1:this.startNavPage-2 ;
        }
    }
}
