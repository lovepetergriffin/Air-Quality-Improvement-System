package com.cqupt.nep;

        import org.mybatis.spring.annotation.MapperScan;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

        @SpringBootApplication
        @MapperScan("com.cqupt.nep.mapper")
        public class NepBackendApplication {
            public static void main(String[] args) {
                SpringApplication.run(NepBackendApplication.class, args);
            }
        }
