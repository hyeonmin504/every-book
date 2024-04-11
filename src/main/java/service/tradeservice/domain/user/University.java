package service.tradeservice.domain.user;


public enum University {
    SAMYOOK("삼육대"), SEOUL("서울대");

    private final String university;

    public String getUniversity() {
        return university;
    }

    University(String university) {
        this.university = university;
    }
}
