package com.cqupt.nep.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.nep.dto.AqiDistributionDto;
import com.cqupt.nep.dto.AqiTrendDto;
import com.cqupt.nep.dto.ConfirmAqiDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.dto.ProvinceStatDto;
import com.cqupt.nep.mapper.StatisticsMapper;
import com.cqupt.nep.pojo.Aqi;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.pojo.GridCity;
import com.cqupt.nep.pojo.GridMember;
import com.cqupt.nep.pojo.GridProvince;
import com.cqupt.nep.pojo.Statistics;
import com.cqupt.nep.pojo.Supervisor;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.service.AqiService;
import com.cqupt.nep.service.GridCityService;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.service.GridProvinceService;
import com.cqupt.nep.service.StatisticsService;
import com.cqupt.nep.service.SupervisorService;
import com.cqupt.nep.utils.ResultVo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatisticsServiceImpl extends ServiceImpl<StatisticsMapper, Statistics> implements StatisticsService {
    @Autowired
    private AqiService aqiService;

    @Autowired
    private AqiFeedbackService feedbackService;

    @Autowired
    private GridProvinceService provinceService;

    @Autowired
    private GridCityService cityService;

    @Autowired
    private GridMemberService gridMemberService;

    @Autowired
    private SupervisorService supervisorService;

    @Override
    public ResultVo<Statistics> saveConfirmData(ConfirmAqiDto dto) {
        if (dto == null || dto.getGmId() == null) {
            return ResultVo.fail("网格员编号不能为空");
        }
        if (dto.getSo2Value() == null || dto.getCoValue() == null || dto.getSpmValue() == null) {
            return ResultVo.fail("实测数据不能为空");
        }

        Integer afId = dto.getAfId();
        AqiFeedback feedback = afId == null ? null : feedbackService.getById(afId);
        if (afId != null && feedback == null) {
            return ResultVo.fail("反馈不存在");
        }

        Statistics statistics = new Statistics();
        statistics.setProvinceId(feedback == null ? dto.getProvinceId() : feedback.getProvinceId());
        statistics.setCityId(feedback == null ? dto.getCityId() : feedback.getCityId());
        statistics.setAddress(feedback == null ? dto.getAddress() : feedback.getAddress());
        statistics.setInformation(feedback == null ? dto.getInformation() : feedback.getInformation());
        statistics.setFdId(feedback == null ? dto.getFdId() : String.valueOf(feedback.getAfId()));
        statistics.setGmId(dto.getGmId());
        statistics.setSo2Value(dto.getSo2Value());
        statistics.setCoValue(dto.getCoValue());
        statistics.setSpmValue(dto.getSpmValue());
        statistics.setSo2Level(dto.getSo2Level() == null ? aqiService.resolveLevel(dto.getSo2Value(), null, null) : dto.getSo2Level());
        statistics.setCoLevel(dto.getCoLevel() == null ? aqiService.resolveLevel(null, dto.getCoValue(), null) : dto.getCoLevel());
        statistics.setSpmLevel(dto.getSpmLevel() == null ? aqiService.resolveLevel(null, null, dto.getSpmValue()) : dto.getSpmLevel());
        statistics.setAqiId(dto.getAqiId() == null ? aqiService.resolveLevel(dto.getSo2Value(), dto.getCoValue(), dto.getSpmValue()) : dto.getAqiId());
        statistics.setConfirmDate(isBlank(dto.getConfirmDate()) ? LocalDate.now().toString() : dto.getConfirmDate());
        statistics.setConfirmTime(isBlank(dto.getConfirmTime()) ? LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) : dto.getConfirmTime());
        save(statistics);
        if (afId != null) {
            feedbackService.markConfirmed(afId);
        }
        fill(statistics);
        return ResultVo.ok(statistics, "统计确认数据保存成功");
    }

    @Override
    public ResultVo<List<Statistics>> queryConfirmedByGmId(Integer gmId) {
        if (gmId == null) {
            return ResultVo.fail("网格员编号不能为空");
        }
        List<Statistics> items = list(new QueryWrapper<Statistics>()
                .eq("gm_id", gmId)
                .orderByDesc("confirm_date")
                .orderByDesc("confirm_time")
                .orderByDesc("id"));
        fillList(items);
        return ResultVo.ok(items, "AQI确认数据列表查询成功");
    }

    @Override
    public ResultVo<PageResult<Statistics>> queryPage(int pageNum, int pageSize, Integer provinceId, Integer cityId, String confirmDate) {
        int safePageNum = Math.max(pageNum, 1);
        int safePageSize = Math.min(Math.max(pageSize, 1), 50);
        int offset = (safePageNum - 1) * safePageSize;
        QueryWrapper<Statistics> countWrapper = new QueryWrapper<Statistics>();
        QueryWrapper<Statistics> listWrapper = new QueryWrapper<Statistics>().orderByDesc("confirm_date").orderByDesc("confirm_time").orderByDesc("id");
        applyFilter(countWrapper, provinceId, cityId, confirmDate);
        applyFilter(listWrapper, provinceId, cityId, confirmDate);
        listWrapper.last("LIMIT " + offset + "," + safePageSize);
        List<Statistics> records = list(listWrapper);
        fillList(records);
        return ResultVo.ok(new PageResult<Statistics>(records, count(countWrapper), safePageNum, safePageSize), "AQI确认数据分页查询成功");
    }

    @Override
    public ResultVo<Statistics> queryById(Integer id) {
        if (id == null) {
            return ResultVo.fail("统计编号不能为空");
        }
        Statistics statistics = getById(id);
        if (statistics == null) {
            return ResultVo.fail("统计数据不存在");
        }
        fill(statistics);
        return ResultVo.ok(statistics, "统计数据查询成功");
    }

    @Override
    public ResultVo<List<ProvinceStatDto>> provinceStats() {
        Map<Integer, String> provinceNames = provinceNameMap();
        Map<Integer, String> cityNames = cityNameMap();
        Map<String, ProvinceAccumulator> counter = new TreeMap<String, ProvinceAccumulator>();
        for (Statistics statistics : list()) {
            if (statistics.getProvinceId() == null || statistics.getCityId() == null) {
                continue;
            }
            String key = statistics.getProvinceId() + "-" + statistics.getCityId();
            ProvinceAccumulator acc = counter.get(key);
            if (acc == null) {
                acc = new ProvinceAccumulator(statistics.getProvinceId(), statistics.getCityId());
                counter.put(key, acc);
            }
            acc.add(statistics);
        }
        List<ProvinceStatDto> result = new ArrayList<ProvinceStatDto>();
        for (ProvinceAccumulator acc : counter.values()) {
            result.add(acc.toDto(provinceNames, cityNames));
        }
        return ResultVo.ok(result, "省市污染统计查询成功");
    }

    @Override
    public ResultVo<List<Map<String, Object>>> groupByProvinceAndCity(Integer provinceId, Integer cityId) {
        QueryWrapper<Statistics> wrapper = new QueryWrapper<Statistics>();
        applyFilter(wrapper, provinceId, cityId, null);
        List<Statistics> items = list(wrapper);
        Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
        for (Statistics item : items) {
            Integer key = item.getAqiId() == null ? 0 : item.getAqiId();
            counter.put(key, counter.containsKey(key) ? counter.get(key) + 1 : 1);
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Map.Entry<Integer, Integer> entry : counter.entrySet()) {
            Map<String, Object> row = new HashMap<String, Object>();
            row.put("aqiId", entry.getKey());
            row.put("count", entry.getValue());
            result.add(row);
        }
        return ResultVo.ok(result, "省市AQI统计查询成功");
    }

    @Override
    public ResultVo<List<AqiDistributionDto>> aqiDistribution() {
        List<Statistics> statisticsList = list();
        Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
        int total = 0;
        for (Statistics statistics : statisticsList) {
            Integer aqiId = statistics.getAqiId();
            if (aqiId != null) {
                counter.put(aqiId, counter.containsKey(aqiId) ? counter.get(aqiId) + 1 : 1);
                total++;
            }
        }
        List<AqiDistributionDto> result = new ArrayList<AqiDistributionDto>();
        for (Aqi aqi : aqiService.list()) {
            int count = counter.containsKey(aqi.getAqiId()) ? counter.get(aqi.getAqiId()) : 0;
            AqiDistributionDto dto = new AqiDistributionDto();
            dto.setAqiId(aqi.getAqiId());
            dto.setChineseExplain(aqi.getChineseExplain());
            dto.setColor(aqi.getColor());
            dto.setCount(count);
            dto.setPercentage(total == 0 ? 0.0 : Math.round(count * 10000.0 / total) / 100.0);
            result.add(dto);
        }
        return ResultVo.ok(result, "AQI等级分布统计完成");
    }

    @Override
    public ResultVo<List<AqiTrendDto>> aqiTrend(String type) {
        boolean month = "month".equalsIgnoreCase(type);
        Map<String, TrendAccumulator> counter = new TreeMap<String, TrendAccumulator>();
        for (Statistics statistics : list(new QueryWrapper<Statistics>().orderByAsc("confirm_date"))) {
            if (isBlank(statistics.getConfirmDate()) || statistics.getAqiId() == null) {
                continue;
            }
            String key = statistics.getConfirmDate();
            if (month && key.length() >= 7) {
                key = key.substring(0, 7);
            }
            TrendAccumulator acc = counter.get(key);
            if (acc == null) {
                acc = new TrendAccumulator(key);
                counter.put(key, acc);
            }
            acc.add(statistics.getAqiId());
        }
        List<AqiTrendDto> result = new ArrayList<AqiTrendDto>();
        for (TrendAccumulator acc : counter.values()) {
            result.add(acc.toDto());
        }
        return ResultVo.ok(result, "AQI趋势统计完成");
    }

    private void applyFilter(QueryWrapper<Statistics> wrapper, Integer provinceId, Integer cityId, String confirmDate) {
        if (provinceId != null && provinceId > 0) {
            wrapper.eq("province_id", provinceId);
        }
        if (cityId != null && cityId > 0) {
            wrapper.eq("city_id", cityId);
        }
        if (!isBlank(confirmDate)) {
            wrapper.eq("confirm_date", confirmDate);
        }
    }

    private void fillList(List<Statistics> items) {
        for (Statistics item : items) {
            fill(item);
        }
    }

    private void fill(Statistics statistics) {
        if (statistics == null) {
            return;
        }
        if (statistics.getProvinceId() != null) {
            statistics.setGridProvince(provinceService.getById(statistics.getProvinceId()));
        }
        if (statistics.getCityId() != null) {
            statistics.setGridCity(cityService.getById(statistics.getCityId()));
        }
        if (statistics.getAqiId() != null) {
            statistics.setAqi(aqiService.getById(statistics.getAqiId()));
        }
        if (statistics.getGmId() != null) {
            GridMember member = gridMemberService.getById(statistics.getGmId());
            if (member != null) {
                member.setPassword(null);
            }
            statistics.setGridMember(member);
        }
        if (!isBlank(statistics.getFdId())) {
            String telId = statistics.getFdId();
            try {
                AqiFeedback feedback = feedbackService.getById(Integer.valueOf(statistics.getFdId()));
                if (feedback != null) {
                    telId = feedback.getTelId();
                }
            } catch (NumberFormatException ignored) {
            }
            Supervisor supervisor = supervisorService.getById(telId);
            if (supervisor != null) {
                supervisor.setPassword(null);
            }
            statistics.setSupervisor(supervisor);
        }
    }

    private Map<Integer, String> provinceNameMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (GridProvince province : provinceService.list()) {
            result.put(province.getProvinceId(), province.getProvinceName());
        }
        return result;
    }

    private Map<Integer, String> cityNameMap() {
        Map<Integer, String> result = new HashMap<Integer, String>();
        for (GridCity city : cityService.list()) {
            result.put(city.getCityId(), city.getCityName());
        }
        return result;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }

    private static class ProvinceAccumulator {
        private Integer provinceId;
        private Integer cityId;
        private int count;
        private int so2Total;
        private int coTotal;
        private int spmTotal;
        private int maxAqiLevel;

        ProvinceAccumulator(Integer provinceId, Integer cityId) {
            this.provinceId = provinceId;
            this.cityId = cityId;
        }

        void add(Statistics statistics) {
            count++;
            so2Total += statistics.getSo2Value() == null ? 0 : statistics.getSo2Value();
            coTotal += statistics.getCoValue() == null ? 0 : statistics.getCoValue();
            spmTotal += statistics.getSpmValue() == null ? 0 : statistics.getSpmValue();
            if (statistics.getAqiId() != null) {
                maxAqiLevel = Math.max(maxAqiLevel, statistics.getAqiId());
            }
        }

        ProvinceStatDto toDto(Map<Integer, String> provinceNames, Map<Integer, String> cityNames) {
            ProvinceStatDto dto = new ProvinceStatDto();
            dto.setProvinceId(provinceId);
            dto.setProvinceName(provinceNames.get(provinceId));
            dto.setCityId(cityId);
            dto.setCityName(cityNames.get(cityId));
            dto.setStatCount(count);
            dto.setAvgSo2Value(count == 0 ? 0.0 : Math.round(so2Total * 100.0 / count) / 100.0);
            dto.setAvgCoValue(count == 0 ? 0.0 : Math.round(coTotal * 100.0 / count) / 100.0);
            dto.setAvgSpmValue(count == 0 ? 0.0 : Math.round(spmTotal * 100.0 / count) / 100.0);
            dto.setMaxAqiLevel(maxAqiLevel);
            return dto;
        }
    }

    private static class TrendAccumulator {
        private String date;
        private int count;
        private int totalLevel;
        private int maxLevel;

        TrendAccumulator(String date) {
            this.date = date;
        }

        void add(Integer level) {
            count++;
            totalLevel += level;
            maxLevel = Math.max(maxLevel, level);
        }

        AqiTrendDto toDto() {
            AqiTrendDto dto = new AqiTrendDto();
            dto.setDate(date);
            dto.setRecordCount(count);
            dto.setAvgAqiLevel(count == 0 ? 0.0 : Math.round(totalLevel * 100.0 / count) / 100.0);
            dto.setMaxAqiLevel(maxLevel);
            return dto;
        }
    }
}
