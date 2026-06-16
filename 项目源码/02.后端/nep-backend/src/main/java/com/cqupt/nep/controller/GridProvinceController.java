package com.cqupt.nep.controller;

import com.cqupt.nep.pojo.GridProvince;
import com.cqupt.nep.service.GridProvinceService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/province", "/gridProvince"})
public class GridProvinceController {
    @Autowired
    private GridProvinceService provinceService;

    @RequestMapping(value = {"/list", "/listGridProvinceAll"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ResultVo<List<GridProvince>> list() {
        return provinceService.listAll();
    }
}
