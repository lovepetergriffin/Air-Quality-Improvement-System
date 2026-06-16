package com.cqupt.nep.dto;

public class FeedbackCreateDto {
    private String telId;
    private Integer provinceId;
    private Integer cityId;
    private String address;
    private String information;
    private Integer estimatedGrade;
    private Integer aqiId;

    public String getTelId() {
        return telId;
    }

    public void setTelId(String telId) {
        this.telId = telId;
    }

    public Integer getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Integer provinceId) {
        this.provinceId = provinceId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public Integer getEstimatedGrade() {
        return estimatedGrade == null ? aqiId : estimatedGrade;
    }

    public void setEstimatedGrade(Integer estimatedGrade) {
        this.estimatedGrade = estimatedGrade;
    }

    public Integer getAqiId() {
        return getEstimatedGrade();
    }

    public void setAqiId(Integer aqiId) {
        this.aqiId = aqiId;
    }
}
