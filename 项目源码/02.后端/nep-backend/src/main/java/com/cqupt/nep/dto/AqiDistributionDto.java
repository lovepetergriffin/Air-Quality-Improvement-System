package com.cqupt.nep.dto;

public class AqiDistributionDto {
    private Integer aqiId;
    private String chineseExplain;
    private String color;
    private Integer count;
    private Double percentage;

    public Integer getAqiId() {
        return aqiId;
    }

    public void setAqiId(Integer aqiId) {
        this.aqiId = aqiId;
    }

    public String getChineseExplain() {
        return chineseExplain;
    }

    public void setChineseExplain(String chineseExplain) {
        this.chineseExplain = chineseExplain;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
