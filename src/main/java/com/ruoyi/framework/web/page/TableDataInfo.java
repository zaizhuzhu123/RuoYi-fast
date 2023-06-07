package com.ruoyi.framework.web.page;

import com.laien.demo.response.PageRes;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 * 
 * @author ruoyi
 */
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 总记录数 */
//    private long total;

    /** 列表数据 */
//    private List<?> rows;

    /** 消息状态码 */
    private int code;

    /** 消息内容 */
    private String message;

    private PageRes data;

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
        data = new PageRes();
    }

    /**
     * 分页
     * 
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total)
    {
        data = new PageRes();
        data.setList(list);
        data.setTotal(total);
//        this.rows = list;
//        this.total = total;
    }

//    public long getTotal()
//    {
//        return total;
//    }
//
    public void setTotal(long total)
    {
//        this.total = total;
        data.setTotal(total);

    }

//    public List<?> getRows()
//    {
//        return rows;
//    }
//
    public void setRows(List<?> rows)
    {
        data.setList(rows);
//        this.rows = rows;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public PageRes getData() {
        return data;
    }

    public void setData(PageRes data) {
        this.data = data;
    }
}