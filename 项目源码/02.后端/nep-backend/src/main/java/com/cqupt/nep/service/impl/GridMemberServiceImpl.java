package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.LoginDto;
import com.cqupt.nep.mapper.GridMemberMapper;
import com.cqupt.nep.pojo.GridMember;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GridMemberServiceImpl extends ServiceImpl<GridMemberMapper, GridMember> implements GridMemberService {
    @Override
    public ResultVo<List<GridMember>> available(Integer provinceId, Integer cityId) {
        QueryWrapper<GridMember> wrapper = new QueryWrapper<GridMember>().eq("state", 0).orderByAsc("gm_id");
        if (provinceId != null && provinceId > 0) {
            wrapper.eq("province_id", provinceId);
        }
        if (cityId != null && cityId > 0) {
            wrapper.eq("city_id", cityId);
        }
        return ResultVo.ok(stripPasswords(list(wrapper)), "可指派网格员查询成功");
    }

    @Override
    public ResultVo<GridMember> login(LoginDto dto) {
        if (dto == null || isBlank(dto.getAccount()) || isBlank(dto.getPassword())) {
            return ResultVo.fail("网格员账号和密码不能为空");
        }
        GridMember member = getOne(new QueryWrapper<GridMember>()
                .eq("gm_code", dto.getAccount())
                .eq("password", dto.getPassword()), false);
        if (member == null) {
            return ResultVo.fail("网格员账号或密码错误");
        }
        member.setPassword(null);
        return ResultVo.ok(member, "网格员登录成功");
    }

    @Override
    public ResultVo<List<GridMember>> queryList(Integer provinceId, Integer cityId, Integer state) {
        QueryWrapper<GridMember> wrapper = new QueryWrapper<GridMember>().orderByAsc("gm_id");
        if (provinceId != null && provinceId > 0) {
            wrapper.eq("province_id", provinceId);
        }
        if (cityId != null && cityId > 0) {
            wrapper.eq("city_id", cityId);
        }
        if (state != null) {
            wrapper.eq("state", state);
        }
        return ResultVo.ok(stripPasswords(list(wrapper)), "网格员列表查询成功");
    }

    @Override
    public ResultVo<GridMember> saveOrUpdateMember(GridMember member) {
        if (member == null || isBlank(member.getGmCode())) {
            return ResultVo.fail("网格员登录编码不能为空");
        }
        if (member.getGmId() == null && isBlank(member.getPassword())) {
            member.setPassword("123");
        }
        if (member.getState() == null) {
            member.setState(0);
        }
        saveOrUpdate(member);
        member.setPassword(null);
        return ResultVo.ok(member, "网格员信息保存成功");
    }

    @Override
    public ResultVo<Boolean> updateState(Integer gmId, Integer state) {
        if (gmId == null || state == null) {
            return ResultVo.fail("网格员编号和状态不能为空");
        }
        boolean updated = update(new UpdateWrapper<GridMember>()
                .eq("gm_id", gmId)
                .set("state", state));
        return ResultVo.ok(updated, "网格员状态更新成功");
    }

    private List<GridMember> stripPasswords(List<GridMember> members) {
        for (GridMember member : members) {
            member.setPassword(null);
        }
        return members;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
