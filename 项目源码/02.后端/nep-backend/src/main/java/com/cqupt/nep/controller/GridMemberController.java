package com.cqupt.nep.controller;

import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.pojo.GridMember;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/grid-member", "/gridMember"})
public class GridMemberController {
    @Autowired
    private GridMemberService gridMemberService;

    @Autowired
    private AqiFeedbackService feedbackService;

    @PostMapping("/login")
    public ResultVo<GridMember> login(@RequestBody LoginDto dto) {
        return gridMemberService.login(dto);
    }

    @GetMapping({"/list", "/listGridMemberAll"})
    public ResultVo<List<GridMember>> list(@RequestParam(required = false) Integer provinceId,
                                           @RequestParam(required = false) Integer cityId,
                                           @RequestParam(required = false) Integer state) {
        return gridMemberService.queryList(provinceId, cityId, state);
    }

    @PostMapping({"/list", "/listGridMemberByProvinceId"})
    public ResultVo<List<GridMember>> listPost(@RequestBody(required = false) Map<String, Object> body) {
        return gridMemberService.queryList(intValue(body, "provinceId"), intValue(body, "cityId"), intValue(body, "state"));
    }

    @GetMapping("/available")
    public ResultVo<List<GridMember>> available(@RequestParam(required = false) Integer provinceId,
                                                @RequestParam(required = false) Integer cityId) {
        return gridMemberService.available(provinceId, cityId);
    }

    @PostMapping({"/save", "/saveGridMember"})
    public ResultVo<GridMember> save(@RequestBody GridMember member) {
        return gridMemberService.saveOrUpdateMember(member);
    }

    @PostMapping({"/state", "/updateState"})
    public ResultVo<Boolean> updateState(@RequestParam(required = false) Integer gmId,
                                         @RequestParam(required = false) Integer state,
                                         @RequestBody(required = false) Map<String, Object> body) {
        return gridMemberService.updateState(gmId == null ? intValue(body, "gmId") : gmId,
                state == null ? intValue(body, "state") : state);
    }

    @GetMapping("/{gmId}/tasks")
    public ResultVo<List<AqiFeedback>> tasksByPath(@PathVariable Integer gmId) {
        return feedbackService.queryAssignedTasks(gmId);
    }

    @GetMapping("/tasks")
    public ResultVo<List<AqiFeedback>> tasksByQuery(@RequestParam Integer gmId) {
        return feedbackService.queryAssignedTasks(gmId);
    }

    private Integer intValue(Map<String, Object> body, String key) {
        if (body == null || body.get(key) == null || "".equals(String.valueOf(body.get(key)))) {
            return null;
        }
        return Integer.valueOf(String.valueOf(body.get(key)));
    }
}
