package service.tradeservice.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;

    private String nickName;
}
