package com.cqupt.nep.service.impl;

        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import com.cqupt.nep.mapper.GridProvinceMapper;
        import com.cqupt.nep.pojo.GridProvince;
        import com.cqupt.nep.service.GridProvinceService;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;
        import org.springframework.stereotype.Service;

        @Service
        public class GridProvinceServiceImpl extends ServiceImpl<GridProvinceMapper, GridProvince> implements GridProvinceService {
            @Override
            public ResultVo<List<GridProvince>> listAll() {
                return ResultVo.ok(list(), "省份列表查询成功");
            }
        }
