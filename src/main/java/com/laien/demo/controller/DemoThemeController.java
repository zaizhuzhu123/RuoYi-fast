package com.laien.demo.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.laien.demo.entity.DemoTheme;
import com.laien.demo.service.IDemoThemeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.laien.demo.controller.base.ResponseController;
import com.laien.demo.controller.base.ResponseResult;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.laien.demo.response.PageRes;

/**
 * 主题Controller
 *
 * @author qmf
 * @date 2023-06-07
 */
@Slf4j
@Api(tags = "主题")
@Controller
@RequestMapping("/demoTheme")
public class DemoThemeController extends ResponseController
{
    private String prefix = "demo/theme";

    @Autowired
    private IDemoThemeService demoThemeService;

    @RequiresPermissions("demo:theme:view")
    @GetMapping()
    public String theme()
    {
        return prefix + "/theme";
    }

    /**
     * 查询主题列表
     */
    @RequiresPermissions("demo:theme:list")
    @PostMapping("/list")
    @ResponseBody
    public ResponseResult<PageRes<DemoTheme>> list(Integer pageNum,Integer pageSize,DemoTheme demoTheme)
    {

        PageRes<DemoTheme> page = demoThemeService.selectDemoThemeList(pageNum, pageSize, demoTheme);
        return succ(page);
    }

    /**
     * 导出主题列表
     */
    @RequiresPermissions("demo:theme:export")
    @Log(title = "主题", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public ResponseResult export(DemoTheme demoTheme)
    {
        PageRes<DemoTheme> page = demoThemeService.selectDemoThemeList(1, 10000, demoTheme);
        ExcelUtil<DemoTheme> util = new ExcelUtil<DemoTheme>(DemoTheme.class);
        AjaxResult result = util.exportExcel(page.getList(), "主题数据");
        return succ(String.valueOf(result.get(AjaxResult.MSG_TAG)), null);
    }

    /**
     * 新增主题
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存主题
     */
    @RequiresPermissions("demo:theme:add")
    @Log(title = "主题", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public ResponseResult<Integer> addSave(DemoTheme demoTheme)
    {
        return succ(demoThemeService.insertDemoTheme(demoTheme));
    }

    /**
     * 修改主题
     */
    @RequiresPermissions("demo:theme:edit")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        DemoTheme demoTheme = demoThemeService.selectDemoThemeById(id);
        mmap.put("demoTheme", demoTheme);
        return prefix + "/edit";
    }

    /**
     * 修改保存主题
     */
    @RequiresPermissions("demo:theme:edit")
    @Log(title = "主题", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public ResponseResult<Integer> editSave(DemoTheme demoTheme)
    {
        return succ(demoThemeService.updateDemoTheme(demoTheme));
    }

    /**
     * 删除主题
     */
    @RequiresPermissions("demo:theme:remove")
    @Log(title = "主题", businessType = BusinessType.DELETE)
    @PostMapping( "/remove")
    @ResponseBody
    public ResponseResult<Integer> remove(String ids)
    {
        return succ(demoThemeService.deleteDemoThemeByIds(ids));
    }

}