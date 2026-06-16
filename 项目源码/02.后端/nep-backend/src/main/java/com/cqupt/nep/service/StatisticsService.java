package com.cqupt.nep.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cqupt.nep.dto.ConfirmAqiDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.dto.ProvinceStatDto;
import com.cqupt.nep.dto.AqiDistributionDto;
import com.cqupt.nep.dto.AqiTrendDto;
import com.cqupt.nep.pojo.Statistics;
import com.cqupt.nep.utils.ResultVo;
import java.util.List;
import java.util.Map;

public interface StatisticsService extends IService<Statistics> {
    ResultVo<Statistics> saveConfirmData(ConfirmAqiDto dto);
    ResultVo<List<Statistics>> queryConfirmedByGmId(Integer gmId);
    ResultVo<PageResult<Statistics>> queryPage(int pageNum, int pageSize, Integer provinceId, Integer cityId, String confirmDate);
    ResultVo<Statistics> queryById(Integer id);
    ResultVo<List<ProvinceStatDto>> provinceStats();
    ResultVo<List<Map<String, Object>>> groupByProvinceAndCity(Integer provinceId, Integer cityId);

    ResultVo<List<AqiDistributionDto>> aqiDistribution();
    ResultVo<List<AqiTrendDto>> aqiTrend(String type);
}
