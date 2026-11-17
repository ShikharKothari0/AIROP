package Project.AIROP.service;

import Project.AIROP.dto.LoginRequest;
import Project.AIROP.dto.LoginResponse;
import Project.AIROP.entity.User;
import Project.AIROP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request){
        User user= userRepository.findByEmail(request.getEmail()).orElseThrow(()-> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getId().toString(), user.getEmail(), user.getRole());
        return new LoginResponse(token, user.getEmail(), user.getRole());
    }


}
