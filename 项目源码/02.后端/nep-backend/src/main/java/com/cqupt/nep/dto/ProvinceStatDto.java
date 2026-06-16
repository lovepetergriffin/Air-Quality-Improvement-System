package com.cqupt.nep.dto;

public class ProvinceStatDto {
    private Integer provinceId;
    private String provinceName;
    private Integer cityId;
    private String cityName;
    private Integer statCount;
    private Double avgSo2Value;
    private Double avgCoValue;
    private Double avgSpmValue;
    private Integer maxAqiLevel;

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getStatCount() {
        return statCount;
    }

    public void setStatCount(Integer statCount) {
        this.statCount = statCount;
    }

    public Double getAvgSo2Value() {
        return avgSo2Value;
    }

    public void setAvgSo2Value(Double avgSo2Value) {
        this.avgSo2Value = avgSo2Value;
    }

    public Double getAvgCoValue() {
        return avgCoValue;
    }

    public void setAvgCoValue(Double avgCoValue) {
        this.avgCoValue = avgCoValue;
    }

    public Double getAvgSpmValue() {
        return avgSpmValue;
    }

    public void setAvgSpmValue(Double avgSpmValue) {
        this.avgSpmValue = avgSpmValue;
    }

    public Integer getMaxAqiLevel() {
        return maxAqiLevel;
    }

    public void setMaxAqiLevel(Integer maxAqiLevel) {
        this.maxAqiLevel = maxAqiLevel;
    }
}
