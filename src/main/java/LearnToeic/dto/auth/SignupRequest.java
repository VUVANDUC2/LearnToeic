package LearnToeic.dto.auth;

import jakarta.validation.constraints.*;
import lombok.Data;
@Data
public class SignupRequest {

    @NotBlank @Size(min = 3, max = 32)
    private String fullName;

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 6, max = 64)
    private String password;

    @NotBlank @Size(min = 6, max = 64)
    private String confirmPassword;

    // GETTERS & SETTERS
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
}
