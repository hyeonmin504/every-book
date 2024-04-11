package service.tradeservice.login;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import service.tradeservice.domain.user.University;

import java.util.ArrayList;
import java.util.List;

@Data
public class SignUpForm {


    @NotEmpty
    private String nickName;

    @Enumerated(EnumType.STRING)
    private University university;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String password;


    public static List<University> university() {
        List<University> univ = new ArrayList<>();
        univ.add(University.SAMYOOK);
        univ.add(University.SEOUL);
        return univ;
    }
}
