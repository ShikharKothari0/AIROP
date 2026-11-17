package Project.AIROP.service;

import Project.AIROP.dto.UserRequest;
import Project.AIROP.dto.UserResponse;
import Project.AIROP.entity.User;
import Project.AIROP.exception.UserNotFoundException;
import Project.AIROP.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use" + request.getEmail());
        }
        ;
        User user=new User();
        user.setId(UUID.randomUUID());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // NOTE: plain text for now — we'll hash this in Phase 2 (Security)
        user.setRole(request.getRole());
//        LocalDateTime now = LocalDateTime.now();
//        user.setCreatedAt(now);
//        user.setUpdatedAt(now);
        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    public UserResponse getUserById(UUID id){
        User user= findUserOrThrow(id);
        return toResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse updateUser(UUID id, UserRequest request){
        User existingUser=findUserOrThrow(id);
        existingUser.setName(request.getName());
        existingUser.setRole(request.getRole());
        existingUser.setUpdatedAt(LocalDateTime.now());
        User updated= userRepository.save(existingUser);
        return toResponse(updated);
    }

    public void deleteUser(UUID id){
        User existingUser=findUserOrThrow(id);
        userRepository.delete(existingUser);
    }

    private User findUserOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }


}
