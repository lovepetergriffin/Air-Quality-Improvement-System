package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.dto.SupervisorRegisterDto;
import com.cqupt.nep.mapper.SupervisorMapper;
import com.cqupt.nep.pojo.Supervisor;
import com.cqupt.nep.service.SupervisorService;
import com.cqupt.nep.utils.ResultVo;
import org.springframework.stereotype.Service;

@Service
public class SupervisorServiceImpl extends ServiceImpl<SupervisorMapper, Supervisor> implements SupervisorService {
    @Override
    public ResultVo<Supervisor> register(SupervisorRegisterDto dto) {
        if (dto == null || isBlank(dto.getTelId()) || isBlank(dto.getPassword())) {
            return ResultVo.fail("手机号和密码不能为空");
        }
        if (getById(dto.getTelId()) != null) {
            return ResultVo.fail("该手机号已注册");
        }
        Supervisor supervisor = new Supervisor();
        supervisor.setTelId(dto.getTelId());
        supervisor.setPassword(dto.getPassword());
        supervisor.setRealName(dto.getRealName());
        supervisor.setBirthday(dto.getBirthday());
        supervisor.setSex(dto.getSex() == null ? 1 : dto.getSex());
        save(supervisor);
        supervisor.setPassword(null);
        return ResultVo.ok(supervisor, "公众监督员注册成功");
    }

    @Override
    public ResultVo<Supervisor> login(LoginDto dto) {
        if (dto == null || isBlank(dto.getAccount()) || isBlank(dto.getPassword())) {
            return ResultVo.fail("手机号或密码不能为空");
        }
        Supervisor supervisor = getOne(new QueryWrapper<Supervisor>()
                .eq("tel_id", dto.getAccount())
                .eq("password", dto.getPassword()), false);
        if (supervisor == null) {
            return ResultVo.fail("手机号或密码不正确");
        }
        supervisor.setPassword(null);
        return ResultVo.ok(supervisor, "登录成功");
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
