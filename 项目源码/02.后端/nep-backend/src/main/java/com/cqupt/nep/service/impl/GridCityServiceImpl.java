package com.cqupt.nep.service.impl;

        import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
        import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
        import com.cqupt.nep.mapper.GridCityMapper;
        import com.cqupt.nep.pojo.GridCity;
        import com.cqupt.nep.service.GridCityService;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;
        import org.springframework.stereotype.Service;

        @Service
        public class GridCityServiceImpl extends ServiceImpl<GridCityMapper, GridCity> implements GridCityService {
            @Override
            public ResultVo<List<GridCity>> listByProvince(Integer provinceId) {
                QueryWrapper<GridCity> wrapper = new QueryWrapper<GridCity>();
                if (provinceId != null) {
                    wrapper.eq("province_id", provinceId);
                }
                return ResultVo.ok(list(wrapper), "城市列表查询成功");
            }
        }
