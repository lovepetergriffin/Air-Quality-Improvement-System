package com.cqupt.nep.controller;

import com.cqupt.nep.pojo.GridCity;
import com.cqupt.nep.service.GridCityService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/city", "/gridCity"})
public class GridCityController {
    @Autowired
    private GridCityService cityService;

    @GetMapping({"/list", "/listGridCityByProvinceId"})
    public ResultVo<List<GridCity>> list(@RequestParam(required = false) Integer provinceId) {
        return cityService.listByProvince(provinceId);
    }

    @PostMapping({"/list", "/listGridCityByProvinceId"})
    public ResultVo<List<GridCity>> listPost(@RequestBody(required = false) Map<String, Object> body) {
        return cityService.listByProvince(intValue(body, "provinceId"));
    }

    private Integer intValue(Map<String, Object> body, String key) {
        if (body == null || body.get(key) == null || "".equals(String.valueOf(body.get(key)))) {
            return null;
        }
        return Integer.valueOf(String.valueOf(body.get(key)));
    }
}
