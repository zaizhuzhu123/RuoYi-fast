package com.laien.demo.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.laien.demo.entity.DemoTheme;

/**
 * 主题Mapper接口
 *
 * @author qmf
 * @date 2023-06-07
 */
public interface DemoThemeMapper extends BaseMapper<DemoTheme> {
    /**
     * 查询主题
     *
     * @param id 主题主键
     * @return 主题
     */
    public DemoTheme selectDemoThemeById(Long id);

    /**
     * 查询主题列表
     *
     * @param demoTheme 主题
     * @return 主题集合
     */
    public List<DemoTheme> selectDemoThemeList(DemoTheme demoTheme);
}