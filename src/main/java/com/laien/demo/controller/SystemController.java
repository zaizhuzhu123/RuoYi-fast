package com.laien.demo.controller;


import com.laien.demo.controller.base.ResponseController;
import com.laien.demo.controller.base.ResponseResult;
import com.laien.demo.entity.FileMd5;
import com.laien.demo.service.IFileMd5Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Slf4j
@Api(tags = "System")
@RestController
@RequestMapping("/system")
public class SystemController extends ResponseController {

    @Resource
    private IFileMd5Service fileMd5Service;

    @ApiOperation(value = "测试")
    @GetMapping("/test")
    public ResponseResult<Void> downloadAllAstriaImages() throws Exception {
        FileMd5 asdasd = fileMd5Service.getOneByMd5("asdasd");
        return succ();
    }

}
