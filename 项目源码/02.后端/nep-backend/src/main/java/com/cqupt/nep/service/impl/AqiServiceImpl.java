package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.AqiQueryDto;
import com.cqupt.nep.mapper.AqiMapper;
import com.cqupt.nep.pojo.Aqi;
import com.cqupt.nep.service.AqiService;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AqiServiceImpl extends ServiceImpl<AqiMapper, Aqi> implements AqiService {
    @Override
    public ResultVo<List<Aqi>> listAll() {
        return ResultVo.ok(list(), "AQI等级列表查询成功");
    }

    @Override
    public ResultVo<Aqi> matchLevel(AqiQueryDto dto) {
        if (dto == null) {
            return ResultVo.fail("查询参数不能为空");
        }
        Aqi aqi = getById(resolveLevel(dto.getSo2Value(), dto.getCoValue(), dto.getSpmValue()));
        return ResultVo.ok(aqi, "AQI等级计算完成");
    }

    @Override
    public int resolveLevel(Integer so2Value, Integer coValue, Integer spmValue) {
        int result = 1;
        for (Aqi level : list()) {
            if (inRange(so2Value, level.getSo2Min(), level.getSo2Max())
                    || inRange(coValue, level.getCoMin(), level.getCoMax())
                    || inRange(spmValue, level.getSpmMin(), level.getSpmMax())) {
                result = Math.max(result, level.getAqiId());
            }
        }
        return result;
    }

    private boolean inRange(Integer value, Integer min, Integer max) {
        return value != null && min != null && max != null && value >= min && value <= max;
    }
}
