package iuh.fit.configs;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

//    @Bean
//    public ModelMapper modelMapper() {
//        ModelMapper modelMapper = new ModelMapper();
//        modelMapper.typeMap(iuh.fit.entities.User.class, iuh.fit.dtos.user.UserDTO.class)
//                .addMappings(mapper -> {
//                });
//
//        return modelMapper;
//    }
}
