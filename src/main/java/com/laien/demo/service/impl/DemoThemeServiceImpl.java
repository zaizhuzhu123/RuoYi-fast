package com.laien.demo.service.impl;


import java.util.List;
import com.google.common.collect.Lists;
import com.ruoyi.common.utils.DateUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.laien.demo.entity.DemoTheme;
import com.laien.demo.mapper.DemoThemeMapper;
import com.laien.demo.service.IDemoThemeService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.laien.demo.constant.GlobalConstant;
import com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;

import com.ruoyi.common.utils.text.Convert;

/**
 * 主题Service业务层处理
 *
 * @author qmf
 * @date 2023-06-07
 */
@Service
public class DemoThemeServiceImpl extends ServiceImpl<DemoThemeMapper, DemoTheme> implements IDemoThemeService
{
    @Resource
    private DemoThemeMapper demoThemeMapper;

    /**
     * 查询主题
     *
     * @param id 主题主键
     * @return 主题
     */
    @Override
    public DemoTheme selectDemoThemeById(Long id)
    {
        return getById(id);
    }

    /**
     * 查询主题列表
     *
     * @param demoTheme 主题
     * @return 主题
     */
    @Override
    public List<DemoTheme> selectDemoThemeList(DemoTheme demoTheme)
    {
        return demoThemeMapper.selectDemoThemeList(demoTheme);
    }

    /**
     * 新增主题
     *
     * @param demoTheme 主题
     * @return 结果
     */
    @Override
    public int insertDemoTheme(DemoTheme demoTheme)
    {
        return save(demoTheme) ? GlobalConstant.YES : GlobalConstant.NO;
    }

    /**
     * 修改主题
     *
     * @param demoTheme 主题
     * @return 结果
     */
    @Override
    public int updateDemoTheme(DemoTheme demoTheme)
    {
        LambdaUpdateWrapper<DemoTheme> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(DemoTheme::getId, demoTheme.getId());
        return update(demoTheme, updateWrapper) ? GlobalConstant.YES : GlobalConstant.NO;
    }

    /**
     * 批量删除主题
     *
     * @param ids 需要删除的主题主键
     * @return 结果
     */

    @Override
    public int deleteDemoThemeByIds(String ids)
    {
        boolean b = removeByIds(Lists.newArrayList(Convert.toStrArray(ids)));
        return b ? GlobalConstant.YES : GlobalConstant.NO;
    }

    /**
     * 删除主题信息
     *
     * @param id 主题主键
     * @return 结果
     */
    @Override
    public int deleteDemoThemeById(Long id)
    {
        boolean b = removeById(id);
        return b ? GlobalConstant.YES : GlobalConstant.NO;
    }
}