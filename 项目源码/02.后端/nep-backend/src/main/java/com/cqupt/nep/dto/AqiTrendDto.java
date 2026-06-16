package com.cqupt.nep.dto;

public class AqiTrendDto {
    private String date;
    private Double avgAqiLevel;
    private Integer maxAqiLevel;
    private Integer recordCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getAvgAqiLevel() {
        return avgAqiLevel;
    }

    public void setAvgAqiLevel(Double avgAqiLevel) {
        this.avgAqiLevel = avgAqiLevel;
    }

    public Integer getMaxAqiLevel() {
        return maxAqiLevel;
    }

    public void setMaxAqiLevel(Integer maxAqiLevel) {
        this.maxAqiLevel = maxAqiLevel;
    }

    public Integer getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(Integer recordCount) {
        this.recordCount = recordCount;
    }
}
