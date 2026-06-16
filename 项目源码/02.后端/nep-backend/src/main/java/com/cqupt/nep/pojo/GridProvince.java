package com.cqupt.nep.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("grid_province")
public class GridProvince {
    @TableId(value = "province_id", type = IdType.AUTO)
    private Integer provinceId;

    @TableField("province_name")
    private String provinceName;

    @TableField("province_abbr")
    private String provinceAbbr;

    @TableField("remarks")
    private String remarks;

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

    public String getProvinceAbbr() {
        return provinceAbbr;
    }

    public void setProvinceAbbr(String provinceAbbr) {
        this.provinceAbbr = provinceAbbr;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
