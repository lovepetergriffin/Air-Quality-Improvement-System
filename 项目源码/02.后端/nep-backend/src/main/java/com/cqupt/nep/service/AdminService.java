package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.dto.LoginDto;
        import com.cqupt.nep.pojo.Admins;
        import com.cqupt.nep.utils.ResultVo;

        public interface AdminService extends IService<Admins> {
            ResultVo<Admins> login(LoginDto dto);
        }
