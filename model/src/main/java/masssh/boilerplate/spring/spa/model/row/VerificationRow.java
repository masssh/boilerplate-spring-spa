package masssh.boilerplate.spring.spa.model.row;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import masssh.boilerplate.spring.spa.enums.VerificationType;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationRow implements Serializable {
    private long verificationId;
    private String verificationHash;
    private long userId;
    private VerificationType verificationType;
    private Instant expiresAt;
    private Instant createdAt;
    private Instant updatedAt;
}
