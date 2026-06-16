package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.dto.AqiQueryDto;
        import com.cqupt.nep.pojo.Aqi;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;

        public interface AqiService extends IService<Aqi> {
            ResultVo<List<Aqi>> listAll();
            ResultVo<Aqi> matchLevel(AqiQueryDto dto);
            int resolveLevel(Integer so2Value, Integer coValue, Integer spmValue);
        }
