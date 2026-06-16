package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.pojo.GridProvince;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;

        public interface GridProvinceService extends IService<GridProvince> {
            ResultVo<List<GridProvince>> listAll();
        }
