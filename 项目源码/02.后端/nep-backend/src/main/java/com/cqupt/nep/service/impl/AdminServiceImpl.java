package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.mapper.AdminsMapper;
import com.cqupt.nep.pojo.Admins;
import com.cqupt.nep.service.AdminService;
import com.cqupt.nep.utils.ResultVo;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl extends ServiceImpl<AdminsMapper, Admins> implements AdminService {
    @Override
    public ResultVo<Admins> login(LoginDto dto) {
        if (dto == null || isBlank(dto.getAccount()) || isBlank(dto.getPassword())) {
            return ResultVo.fail("账号或密码不能为空");
        }
        Admins admin = getOne(new QueryWrapper<Admins>()
                .eq("admin_code", dto.getAccount())
                .eq("password", dto.getPassword()), false);
        if (admin == null) {
            return ResultVo.fail("管理员账号或密码错误");
        }
        admin.setPassword(null);
        return ResultVo.ok(admin, "登录成功");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
