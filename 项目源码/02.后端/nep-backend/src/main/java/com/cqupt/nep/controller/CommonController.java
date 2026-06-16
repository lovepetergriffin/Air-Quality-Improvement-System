package com.cqupt.nep.controller;

import com.cqupt.nep.pojo.Aqi;
import com.cqupt.nep.pojo.GridCity;
import com.cqupt.nep.pojo.GridMember;
import com.cqupt.nep.pojo.GridProvince;
import com.cqupt.nep.service.AqiService;
import com.cqupt.nep.service.GridCityService;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.service.GridProvinceService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private AqiService aqiService;

    @Autowired
    private GridProvinceService provinceService;

    @Autowired
    private GridCityService cityService;

    @Autowired
    private GridMemberService gridMemberService;

    @GetMapping({"/aqi-list", "/aqiList"})
    public ResultVo<List<Aqi>> aqiList() {
        return aqiService.listAll();
    }

    @GetMapping({"/province-list", "/provinceList"})
    public ResultVo<List<GridProvince>> provinceList() {
        return provinceService.listAll();
    }

    @GetMapping({"/city-list", "/cityList"})
    public ResultVo<List<GridCity>> cityList(@RequestParam(required = false) Integer provinceId) {
        return cityService.listByProvince(provinceId);
    }

    @GetMapping({"/grid-member-list", "/gridMemberList"})
    public ResultVo<List<GridMember>> gridMemberList(@RequestParam(required = false) Integer provinceId,
                                                     @RequestParam(required = false) Integer cityId) {
        return gridMemberService.available(provinceId, cityId);
    }
}
