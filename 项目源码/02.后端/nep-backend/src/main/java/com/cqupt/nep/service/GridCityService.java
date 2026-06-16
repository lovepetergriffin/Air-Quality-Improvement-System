package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.pojo.GridCity;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;

        public interface GridCityService extends IService<GridCity> {
            ResultVo<List<GridCity>> listByProvince(Integer provinceId);
        }
