package com.cqupt.nep.controller;

import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.pojo.Admins;
import com.cqupt.nep.service.AdminService;
import com.cqupt.nep.utils.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/admin", "/admins"})
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping({"/login", "/getAdminsByCodeByPass"})
    public ResultVo<Admins> login(@RequestBody LoginDto dto) {
        return adminService.login(dto);
    }
}
