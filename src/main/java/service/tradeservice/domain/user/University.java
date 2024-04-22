package service.tradeservice.domain.user;


import lombok.Getter;

@Getter
public enum University {
    SAMYOOK("삼육대"), SEOUL("서울대");

    private final String university;

    University(String university) {
        this.university = university;
    }
}
