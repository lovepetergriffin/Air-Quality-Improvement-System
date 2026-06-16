package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.AssignFeedbackDto;
import com.cqupt.nep.dto.FeedbackCreateDto;
import com.cqupt.nep.dto.FeedbackQueryDto;
import com.cqupt.nep.dto.FeedbackUpdateDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.mapper.AqiFeedbackMapper;
import com.cqupt.nep.pojo.Aqi;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.pojo.GridMember;
import com.cqupt.nep.pojo.Supervisor;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.service.AqiService;
import com.cqupt.nep.service.GridCityService;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.service.GridProvinceService;
import com.cqupt.nep.service.SupervisorService;
import com.cqupt.nep.utils.ResultVo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AqiFeedbackServiceImpl extends ServiceImpl<AqiFeedbackMapper, AqiFeedback> implements AqiFeedbackService {
    @Autowired
    private SupervisorService supervisorService;

    @Autowired
    private GridProvinceService provinceService;

    @Autowired
    private GridCityService cityService;

    @Autowired
    private AqiService aqiService;

    @Autowired
    private GridMemberService gridMemberService;

    @Override
    public ResultVo<List<AqiFeedback>> queryList(FeedbackQueryDto dto) {
        List<AqiFeedback> items = list(buildListWrapper(dto));
        fillList(items);
        return ResultVo.ok(items, "反馈列表查询成功");
    }

    @Override
    public ResultVo<PageResult<AqiFeedback>> queryPage(int pageNum, int pageSize, Integer state, Integer provinceId) {
        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        int offset = (safePageNum - 1) * safePageSize;

        QueryWrapper<AqiFeedback> countWrapper = new QueryWrapper<AqiFeedback>();
        QueryWrapper<AqiFeedback> listWrapper = new QueryWrapper<AqiFeedback>().orderByDesc("af_id");
        applyFilter(countWrapper, state, provinceId, null, null);
        applyFilter(listWrapper, state, provinceId, null, null);
        listWrapper.last("LIMIT " + offset + "," + safePageSize);

        List<AqiFeedback> records = list(listWrapper);
        fillList(records);
        return ResultVo.ok(new PageResult<AqiFeedback>(records, count(countWrapper), safePageNum, safePageSize), "反馈分页查询成功");
    }

    @Override
    public ResultVo<List<AqiFeedback>> queryAssignedTasks(Integer gmId) {
        if (gmId == null) {
            return ResultVo.fail("网格员编号不能为空");
        }
        List<AqiFeedback> items = list(new QueryWrapper<AqiFeedback>()
                .eq("gm_id", gmId)
                .eq("state", 1)
                .orderByDesc("assign_date")
                .orderByDesc("assign_time")
                .orderByDesc("af_id"));
        fillList(items);
        return ResultVo.ok(items, "网格员任务列表查询成功");
    }

    @Override
    public ResultVo<List<AqiFeedback>> queryBySupervisor(String telId) {
        if (isBlank(telId)) {
            return ResultVo.fail("监督员手机号不能为空");
        }
        List<AqiFeedback> items = list(new QueryWrapper<AqiFeedback>()
                .eq("tel_id", telId)
                .orderByDesc("af_date")
                .orderByDesc("af_time")
                .orderByDesc("af_id"));
        fillList(items);
        return ResultVo.ok(items, "公众监督员反馈记录查询成功");
    }

    @Override
    public ResultVo<AqiFeedback> detail(Integer afId) {
        if (afId == null) {
            return ResultVo.fail("反馈编号不能为空");
        }
        AqiFeedback feedback = getById(afId);
        if (feedback == null) {
            return ResultVo.fail("反馈不存在");
        }
        fill(feedback);
        return ResultVo.ok(feedback, "反馈详情查询成功");
    }

    @Override
    public ResultVo<AqiFeedback> createFeedback(FeedbackCreateDto dto) {
        if (dto == null || isBlank(dto.getTelId())) {
            return ResultVo.fail("反馈人不能为空");
        }
        if (dto.getProvinceId() == null || dto.getCityId() == null) {
            return ResultVo.fail("省份和城市不能为空");
        }
        if (isBlank(dto.getAddress()) || isBlank(dto.getInformation())) {
            return ResultVo.fail("反馈地址和反馈信息不能为空");
        }
        AqiFeedback feedback = new AqiFeedback();
        feedback.setTelId(dto.getTelId());
        feedback.setProvinceId(dto.getProvinceId());
        feedback.setCityId(dto.getCityId());
        feedback.setAddress(dto.getAddress());
        feedback.setInformation(dto.getInformation());
        feedback.setEstimatedGrade(dto.getEstimatedGrade() == null ? 1 : dto.getEstimatedGrade());
        feedback.setAfDate(LocalDate.now().toString());
        feedback.setAfTime(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        feedback.setGmId(0);
        feedback.setState(0);
        save(feedback);
        fill(feedback);
        return ResultVo.ok(feedback, "公众监督反馈提交成功");
    }

    @Override
    public ResultVo<Boolean> updateFeedback(FeedbackUpdateDto dto) {
        if (dto == null || dto.getAfId() == null) {
            return ResultVo.fail("反馈编号不能为空");
        }
        UpdateWrapper<AqiFeedback> wrapper = new UpdateWrapper<AqiFeedback>().eq("af_id", dto.getAfId());
        if (dto.getProvinceId() != null) {
            wrapper.set("province_id", dto.getProvinceId());
        }
        if (dto.getCityId() != null) {
            wrapper.set("city_id", dto.getCityId());
        }
        if (!isBlank(dto.getAddress())) {
            wrapper.set("address", dto.getAddress());
        }
        if (!isBlank(dto.getInformation())) {
            wrapper.set("information", dto.getInformation());
        }
        if (dto.getEstimatedGrade() != null) {
            wrapper.set("estimated_grade", dto.getEstimatedGrade());
        }
        if (dto.getState() != null) {
            wrapper.set("state", dto.getState());
        }
        return ResultVo.ok(update(wrapper), "反馈信息更新成功");
    }

    @Override
    public ResultVo<Boolean> updateState(Integer afId, Integer state) {
        if (afId == null || state == null) {
            return ResultVo.fail("反馈编号和状态不能为空");
        }
        boolean updated = update(new UpdateWrapper<AqiFeedback>()
                .eq("af_id", afId)
                .set("state", state));
        return ResultVo.ok(updated, "反馈状态更新成功");
    }

    @Override
    public ResultVo<Boolean> assign(AssignFeedbackDto dto) {
        if (dto == null || dto.getAfId() == null || dto.getGmId() == null) {
            return ResultVo.fail("反馈编号和网格员编号不能为空");
        }
        boolean updated = update(new UpdateWrapper<AqiFeedback>()
                .eq("af_id", dto.getAfId())
                .set("gm_id", dto.getGmId())
                .set("assign_date", LocalDate.now().toString())
                .set("assign_time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                .set("state", 1));
        return ResultVo.ok(updated, "反馈派发成功");
    }

    @Override
    public ResultVo<Boolean> markConfirmed(Integer afId) {
        return updateState(afId, 2);
    }

    private QueryWrapper<AqiFeedback> buildListWrapper(FeedbackQueryDto dto) {
        QueryWrapper<AqiFeedback> wrapper = new QueryWrapper<AqiFeedback>().orderByDesc("af_id");
        if (dto != null) {
            applyFilter(wrapper, dto.getState(), dto.getProvinceId(), dto.getCityId(), dto.getTelId());
        }
        return wrapper;
    }

    private void applyFilter(QueryWrapper<AqiFeedback> wrapper, Integer state, Integer provinceId, Integer cityId, String telId) {
        if (state != null) {
            wrapper.eq("state", state);
        }
        if (provinceId != null && provinceId > 0) {
            wrapper.eq("province_id", provinceId);
        }
        if (cityId != null && cityId > 0) {
            wrapper.eq("city_id", cityId);
        }
        if (!isBlank(telId)) {
            wrapper.eq("tel_id", telId);
        }
    }

    private void fillList(List<AqiFeedback> items) {
        for (AqiFeedback item : items) {
            fill(item);
        }
    }

    private void fill(AqiFeedback feedback) {
        if (feedback == null) {
            return;
        }
        if (!isBlank(feedback.getTelId())) {
            Supervisor supervisor = supervisorService.getById(feedback.getTelId());
            if (supervisor != null) {
                supervisor.setPassword(null);
            }
            feedback.setSupervisor(supervisor);
        }
        if (feedback.getProvinceId() != null) {
            feedback.setGridProvince(provinceService.getById(feedback.getProvinceId()));
        }
        if (feedback.getCityId() != null) {
            feedback.setGridCity(cityService.getById(feedback.getCityId()));
        }
        if (feedback.getEstimatedGrade() != null) {
            Aqi aqi = aqiService.getById(feedback.getEstimatedGrade());
            feedback.setAqi(aqi);
        }
        if (feedback.getGmId() != null && feedback.getGmId() > 0) {
            GridMember member = gridMemberService.getById(feedback.getGmId());
            if (member != null) {
                member.setPassword(null);
            }
            feedback.setGridMember(member);
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
