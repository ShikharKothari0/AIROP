package Project.AIROP.service;

import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;

    @Value("${airop.jwt.expiration}")
    private long jwtExpiration;

    public JwtService(JwtEncoder jwtEncoder){
        this.jwtEncoder=jwtEncoder;
    }

    public String generateToken(String userId, String email, String role){
        Instant now= Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userId)
                .claim("email",email)
                .claim("role",role)
                .issuedAt(now)
                .expiresAt(now.plusMillis(jwtExpiration))
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }
}
