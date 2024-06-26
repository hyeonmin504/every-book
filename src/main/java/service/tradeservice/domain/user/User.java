package service.tradeservice.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.Room;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String nickName;
    @Enumerated(EnumType.STRING)
    private University university;

    @Email
    private String email;

    private String password;

    private String joinDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Favorite> favorites = new ArrayList<>();

    private int soldItemCount;

    public void changeSoldItemCount() {
        this.soldItemCount++;
    }

    public User(String nickName, University university, String email, String password) {
        this.nickName = nickName;
        this.university = university;
        this.email = email;
        this.password = password;
        this.joinDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        this.soldItemCount = 0;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
