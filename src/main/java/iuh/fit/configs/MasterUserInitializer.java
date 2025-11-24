// src/main/java/iuh/fit/runner/MasterUserInitializer.java

package iuh.fit.configs;

import iuh.fit.entities.User;
import iuh.fit.entities.enums.Role;
import iuh.fit.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class MasterUserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.master.username}")
    private String masterUsername;

    @Value("${app.master.email}")
    private String masterEmail;

    @Value("${app.master.password}")
    private String masterPassword;

    @Value("${app.master.fullname}")
    private String masterFullName;

    public MasterUserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Chỉ tạo nếu chưa có MASTER
        if (userRepository.findByRole(Role.MASTER).isEmpty()) {
            User master = new User();
            master.setUsername(masterUsername);
            master.setEmail(masterEmail);
            master.setPassword(passwordEncoder.encode(masterPassword));
            master.setFullName(masterFullName);
            master.setRole(Role.MASTER);
            master.setIsActive(true);

            userRepository.save(master);
            System.out.println("MASTER account created: " + masterUsername);
        } else {
            System.out.println("MASTER account already exists. Skipping creation.");
        }
    }
}