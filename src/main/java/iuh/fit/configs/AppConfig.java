package iuh.fit.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class AppConfig {

//    @Bean
//    public ModelMapper modelMapper() {
//        return new ModelMapper();
//    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // Cấu hình để không map password field
        modelMapper.typeMap(iuh.fit.entities.User.class, iuh.fit.dtos.user.UserDTO.class)
                .addMappings(mapper -> mapper.skip(iuh.fit.dtos.user.UserDTO::setId));
        return modelMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
