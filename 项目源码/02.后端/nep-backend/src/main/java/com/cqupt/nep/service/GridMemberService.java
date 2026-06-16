package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.dto.LoginDto;
        import com.cqupt.nep.pojo.GridMember;
        import com.cqupt.nep.utils.ResultVo;
        import java.util.List;

        public interface GridMemberService extends IService<GridMember> {
            ResultVo<List<GridMember>> available(Integer provinceId, Integer cityId);
            ResultVo<GridMember> login(LoginDto dto);
            ResultVo<List<GridMember>> queryList(Integer provinceId, Integer cityId, Integer state);
            ResultVo<GridMember> saveOrUpdateMember(GridMember member);
            ResultVo<Boolean> updateState(Integer gmId, Integer state);
        }
