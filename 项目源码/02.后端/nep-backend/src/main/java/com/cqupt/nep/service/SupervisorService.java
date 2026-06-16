package com.cqupt.nep.service;

        import com.baomidou.mybatisplus.extension.service.IService;
        import com.cqupt.nep.dto.LoginDto;
        import com.cqupt.nep.dto.SupervisorRegisterDto;
        import com.cqupt.nep.pojo.Supervisor;
        import com.cqupt.nep.utils.ResultVo;

        public interface SupervisorService extends IService<Supervisor> {
            ResultVo<Supervisor> register(SupervisorRegisterDto dto);
            ResultVo<Supervisor> login(LoginDto dto);
        }
