package com.cqupt.nep.controller;

import com.cqupt.nep.dto.AqiQueryDto;
import com.cqupt.nep.pojo.Aqi;
import com.cqupt.nep.service.AqiService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/aqi")
public class AqiController {
    @Autowired
    private AqiService aqiService;

    @GetMapping({"/list", "/listAqiAll"})
    public ResultVo<List<Aqi>> list() {
        return aqiService.listAll();
    }

    @PostMapping({"/match", "/getAqiByValue"})
    public ResultVo<Aqi> match(@RequestBody AqiQueryDto dto) {
        return aqiService.matchLevel(dto);
    }
}
