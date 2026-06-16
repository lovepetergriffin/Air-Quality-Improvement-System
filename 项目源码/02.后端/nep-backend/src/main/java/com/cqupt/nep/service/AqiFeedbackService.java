package com.cqupt.nep.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.nep.dto.AssignFeedbackDto;
import com.cqupt.nep.dto.FeedbackCreateDto;
import com.cqupt.nep.dto.FeedbackQueryDto;
import com.cqupt.nep.dto.FeedbackUpdateDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;

public interface AqiFeedbackService extends IService<AqiFeedback> {
    ResultVo<List<AqiFeedback>> queryList(FeedbackQueryDto dto);
    ResultVo<PageResult<AqiFeedback>> queryPage(int pageNum, int pageSize, Integer state, Integer provinceId);
    ResultVo<List<AqiFeedback>> queryAssignedTasks(Integer gmId);
    ResultVo<List<AqiFeedback>> queryBySupervisor(String telId);
    ResultVo<AqiFeedback> detail(Integer afId);
    ResultVo<AqiFeedback> createFeedback(FeedbackCreateDto dto);
    ResultVo<Boolean> updateFeedback(FeedbackUpdateDto dto);
    ResultVo<Boolean> updateState(Integer afId, Integer state);
    ResultVo<Boolean> assign(AssignFeedbackDto dto);
    ResultVo<Boolean> markConfirmed(Integer afId);
}
