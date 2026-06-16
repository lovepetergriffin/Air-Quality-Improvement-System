package com.cqupt.nep.controller;

import com.cqupt.nep.dto.FeedbackCreateDto;
import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.dto.SupervisorRegisterDto;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.pojo.Supervisor;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.service.SupervisorService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {
    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private AqiFeedbackService feedbackService;

    @GetMapping("/list")
    public ResultVo<List<Supervisor>> list() {
        List<Supervisor> supervisors = supervisorService.list();
        for (Supervisor supervisor : supervisors) {
            supervisor.setPassword(null);
        }
        return ResultVo.ok(supervisors, "公众监督员列表查询成功");
    }

    @PostMapping("/register")
    public ResultVo<Supervisor> register(@RequestBody SupervisorRegisterDto dto) {
        return supervisorService.register(dto);
    }

    @PostMapping("/login")
    public ResultVo<Supervisor> login(@RequestBody LoginDto dto) {
        return supervisorService.login(dto);
    }

    @GetMapping({"/exist", "/isExist"})
    public ResultVo<Boolean> exist(@RequestParam String telId) {
        return ResultVo.ok(supervisorService.getById(telId) != null, "手机号检查完成");
    }

    @GetMapping({"/feedback-list", "/feedbackList"})
    public ResultVo<List<AqiFeedback>> feedbackList(@RequestParam String telId) {
        return feedbackService.queryBySupervisor(telId);
    }

    @PostMapping({"/submit-feedback", "/submitFeedback"})
    public ResultVo<AqiFeedback> submitFeedback(@RequestBody FeedbackCreateDto dto) {
        return feedbackService.createFeedback(dto);
    }
}
