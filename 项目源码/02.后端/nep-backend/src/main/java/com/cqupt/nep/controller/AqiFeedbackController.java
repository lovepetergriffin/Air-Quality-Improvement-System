package com.cqupt.nep.controller;

import com.cqupt.nep.dto.AssignFeedbackDto;
import com.cqupt.nep.dto.FeedbackCreateDto;
import com.cqupt.nep.dto.FeedbackQueryDto;
import com.cqupt.nep.dto.FeedbackUpdateDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/feedback", "/aqiFeedback"})
public class AqiFeedbackController {
    @Autowired
    private AqiFeedbackService feedbackService;

    @PostMapping("/list")
    public ResultVo<List<AqiFeedback>> list(@RequestBody(required = false) FeedbackQueryDto dto) {
        return feedbackService.queryList(dto);
    }

    @GetMapping({"/page", "/listAqiFeedbackPage"})
    public ResultVo<PageResult<AqiFeedback>> page(@RequestParam(defaultValue = "1") int pageNum,
                                                  @RequestParam(defaultValue = "10") int pageSize,
                                                  @RequestParam(required = false) Integer state,
                                                  @RequestParam(required = false) Integer provinceId) {
        return feedbackService.queryPage(pageNum, pageSize, state, provinceId);
    }

    @PostMapping("/listAqiFeedbackPage")
    public ResultVo<PageResult<AqiFeedback>> pagePost(@RequestBody(required = false) Map<String, Object> body) {
        return feedbackService.queryPage(defaultInt(body, "pageNum", 1),
                defaultInt(body, "maxPageNum", defaultInt(body, "pageSize", 10)),
                intValue(body, "state"),
                intValue(body, "provinceId"));
    }

    @GetMapping("/{afId:\\d+}")
    public ResultVo<AqiFeedback> detail(@PathVariable Integer afId) {
        return feedbackService.detail(afId);
    }

    @GetMapping({"/detail/{afId}", "/getAqiFeedbackById/{afId}"})
    public ResultVo<AqiFeedback> detailAlias(@PathVariable Integer afId) {
        return feedbackService.detail(afId);
    }

    @PostMapping("/getAqiFeedbackById")
    public ResultVo<AqiFeedback> detailPost(@RequestBody Map<String, Object> body) {
        return feedbackService.detail(intValue(body, "afId"));
    }

    @PostMapping({"/create", "/save"})
    public ResultVo<AqiFeedback> create(@RequestBody FeedbackCreateDto dto) {
        return feedbackService.createFeedback(dto);
    }

    @PostMapping("/update")
    public ResultVo<Boolean> update(@RequestBody FeedbackUpdateDto dto) {
        return feedbackService.updateFeedback(dto);
    }

    @PutMapping("/update")
    public ResultVo<Boolean> updatePut(@RequestBody FeedbackUpdateDto dto) {
        return feedbackService.updateFeedback(dto);
    }

    @PostMapping({"/state", "/updateState"})
    public ResultVo<Boolean> state(@RequestParam(required = false) Integer afId,
                                   @RequestParam(required = false) Integer state,
                                   @RequestBody(required = false) Map<String, Object> body) {
        return feedbackService.updateState(afId == null ? intValue(body, "afId") : afId,
                state == null ? intValue(body, "state") : state);
    }

    @PutMapping("/updateState")
    public ResultVo<Boolean> statePut(@RequestBody(required = false) Map<String, Object> body) {
        return feedbackService.updateState(intValue(body, "afId"), intValue(body, "state"));
    }

    @PostMapping({"/assign", "/updateAqiFeedbackAssign"})
    public ResultVo<Boolean> assign(@RequestBody AssignFeedbackDto dto) {
        return feedbackService.assign(dto);
    }

    @GetMapping({"/assigned", "/listAssignedTasks"})
    public ResultVo<List<AqiFeedback>> assigned(@RequestParam Integer gmId) {
        return feedbackService.queryAssignedTasks(gmId);
    }

    private Integer intValue(Map<String, Object> body, String key) {
        if (body == null || body.get(key) == null || "".equals(String.valueOf(body.get(key)))) {
            return null;
        }
        return Integer.valueOf(String.valueOf(body.get(key)));
    }

    private int defaultInt(Map<String, Object> body, String key, int defaultValue) {
        Integer value = intValue(body, key);
        return value == null ? defaultValue : value;
    }
}
