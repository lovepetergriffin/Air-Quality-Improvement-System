package com.cqupt.nep.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cqupt.nep.dto.AqiDistributionDto;
import com.cqupt.nep.dto.AqiTrendDto;
import com.cqupt.nep.dto.ConfirmAqiDto;
import com.cqupt.nep.dto.PageResult;
import com.cqupt.nep.dto.ProvinceStatDto;
import com.cqupt.nep.pojo.AqiFeedback;
import com.cqupt.nep.pojo.GridCity;
import com.cqupt.nep.pojo.GridProvince;
import com.cqupt.nep.pojo.Statistics;
import com.cqupt.nep.service.AqiFeedbackService;
import com.cqupt.nep.service.GridCityService;
import com.cqupt.nep.service.GridMemberService;
import com.cqupt.nep.service.GridProvinceService;
import com.cqupt.nep.service.StatisticsService;
import com.cqupt.nep.utils.ResultVo;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private AqiFeedbackService feedbackService;

    @Autowired
    private GridMemberService gridMemberService;

    @Autowired
    private GridProvinceService provinceService;

    @Autowired
    private GridCityService cityService;

    @PostMapping({"/confirm", "/save"})
    public ResultVo<Statistics> confirm(@RequestBody ConfirmAqiDto dto) {
        return statisticsService.saveConfirmData(dto);
    }

    @GetMapping("/dashboard")
    public ResultVo<Map<String, Object>> dashboard() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("feedbackTotal", feedbackService.count());
        data.put("unassigned", feedbackService.count(new QueryWrapper<AqiFeedback>().eq("state", 0)));
        data.put("assigned", feedbackService.count(new QueryWrapper<AqiFeedback>().eq("state", 1)));
        data.put("confirmed", feedbackService.count(new QueryWrapper<AqiFeedback>().eq("state", 2)));
        data.put("gridMemberTotal", gridMemberService.count());
        data.put("statisticsTotal", statisticsService.count());
        data.put("provinceCoverage", provinceCoverageValue());
        data.put("cityCoverage", cityCoverageValue());
        data.put("highPriorityPending", feedbackService.count(new QueryWrapper<AqiFeedback>()
                .in("estimated_grade", 5, 6)
                .ne("state", 2)));
        data.put("todayNewFeedback", feedbackService.count(new QueryWrapper<AqiFeedback>()
                .eq("af_date", LocalDate.now().toString())));
        return ResultVo.ok(data, "首页看板数据查询成功");
    }

    @GetMapping("/group")
    public ResultVo<List<Map<String, Object>>> group(@RequestParam(required = false) Integer provinceId,
                                                     @RequestParam(required = false) Integer cityId) {
        return statisticsService.groupByProvinceAndCity(provinceId, cityId);
    }

    @GetMapping({"/confirmed-list", "/confirmedList"})
    public ResultVo<List<Statistics>> confirmedList(@RequestParam Integer gmId) {
        return statisticsService.queryConfirmedByGmId(gmId);
    }

    @GetMapping("/listStatisticsPage")
    public ResultVo<PageResult<Statistics>> listStatisticsPage(@RequestParam(defaultValue = "1") int pageNum,
                                                               @RequestParam(defaultValue = "10") int pageSize,
                                                               @RequestParam(required = false) Integer provinceId,
                                                               @RequestParam(required = false) Integer cityId,
                                                               @RequestParam(required = false) String confirmDate) {
        return statisticsService.queryPage(pageNum, pageSize, provinceId, cityId, confirmDate);
    }

    @PostMapping("/listStatisticsPage")
    public ResultVo<PageResult<Statistics>> listStatisticsPagePost(@RequestBody(required = false) Map<String, Object> body) {
        return statisticsService.queryPage(defaultInt(body, "pageNum", 1),
                defaultInt(body, "maxPageNum", defaultInt(body, "pageSize", 10)),
                intValue(body, "provinceId"),
                intValue(body, "cityId"),
                stringValue(body, "confirmDate"));
    }

    @GetMapping("/{id:\\d+}")
    public ResultVo<Statistics> detail(@PathVariable Integer id) {
        return statisticsService.queryById(id);
    }

    @PostMapping("/getStatisticsById")
    public ResultVo<Statistics> detailPost(@RequestBody Map<String, Object> body) {
        return statisticsService.queryById(intValue(body, "id"));
    }

    @GetMapping({"/province-stats", "/provinceStats", "/listProvinceItemTotalStatis"})
    public ResultVo<List<ProvinceStatDto>> provinceStats() {
        return statisticsService.provinceStats();
    }

    @PostMapping("/listProvinceItemTotalStatis")
    public ResultVo<List<ProvinceStatDto>> provinceStatsPost() {
        return provinceStats();
    }

    @GetMapping({"/aqi-distribution", "/aqiDistribution", "/listAqiDistributeTotalStatis"})
    public ResultVo<List<AqiDistributionDto>> aqiDistribution() {
        return statisticsService.aqiDistribution();
    }

    @PostMapping("/listAqiDistributeTotalStatis")
    public ResultVo<List<AqiDistributionDto>> aqiDistributionPost() {
        return aqiDistribution();
    }

    @GetMapping({"/aqi-trend", "/aqiTrend", "/listAqiTrendTotalStatis"})
    public ResultVo<List<AqiTrendDto>> aqiTrend(@RequestParam(defaultValue = "day") String type) {
        return statisticsService.aqiTrend(type);
    }

    @PostMapping("/listAqiTrendTotalStatis")
    public ResultVo<List<AqiTrendDto>> aqiTrendPost(@RequestBody(required = false) Map<String, Object> body) {
        return statisticsService.aqiTrend(stringValue(body, "type") == null ? "day" : stringValue(body, "type"));
    }

    @GetMapping("/getAqiCount")
    public ResultVo<Long> getAqiCount() {
        return ResultVo.ok(statisticsService.count(), "AQI检测总数查询成功");
    }

    @PostMapping("/getAqiCount")
    public ResultVo<Long> getAqiCountPost() {
        return getAqiCount();
    }

    @GetMapping("/getAqiGoodCount")
    public ResultVo<Long> getAqiGoodCount() {
        return ResultVo.ok(statisticsService.count(new QueryWrapper<Statistics>().le("aqi_id", 2)), "AQI良好数量查询成功");
    }

    @PostMapping("/getAqiGoodCount")
    public ResultVo<Long> getAqiGoodCountPost() {
        return getAqiGoodCount();
    }

    @GetMapping("/getProvinceCoverage")
    public ResultVo<Double> getProvinceCoverage() {
        return ResultVo.ok(provinceCoverageValue(), "省网格覆盖率查询成功");
    }

    @PostMapping("/getProvinceCoverage")
    public ResultVo<Double> getProvinceCoveragePost() {
        return getProvinceCoverage();
    }

    @GetMapping("/getCityCoverage")
    public ResultVo<Double> getCityCoverage() {
        return ResultVo.ok(cityCoverageValue(), "城市网格覆盖率查询成功");
    }

    @PostMapping("/getCityCoverage")
    public ResultVo<Double> getCityCoveragePost() {
        return getCityCoverage();
    }

    private Double provinceCoverageValue() {
        long total = provinceService.count();
        if (total == 0) {
            return 0.0;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (GridProvince province : provinceService.list()) {
            if (statisticsService.count(new QueryWrapper<Statistics>().eq("province_id", province.getProvinceId())) > 0) {
                used.add(province.getProvinceId());
            }
        }
        return Math.round(used.size() * 10000.0 / total) / 100.0;
    }

    private Double cityCoverageValue() {
        long total = cityService.count();
        if (total == 0) {
            return 0.0;
        }
        Set<Integer> used = new HashSet<Integer>();
        for (GridCity city : cityService.list()) {
            if (statisticsService.count(new QueryWrapper<Statistics>().eq("city_id", city.getCityId())) > 0) {
                used.add(city.getCityId());
            }
        }
        return Math.round(used.size() * 10000.0 / total) / 100.0;
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

    private String stringValue(Map<String, Object> body, String key) {
        if (body == null || body.get(key) == null) {
            return null;
        }
        return String.valueOf(body.get(key));
    }
}
