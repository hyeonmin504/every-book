package service.tradeservice.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.NoArgsConstructor;
import service.tradeservice.domain.Favorite;
import service.tradeservice.domain.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
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
    private LocalDateTime joinDate;

    @OneToMany(mappedBy = "user")
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Favorite> favorites = new ArrayList<>();

    public User(String nickName, University university, String email, String password) {
        this.nickName = nickName;
        this.university = university;
        this.email = email;
        this.password = password;
    }
}
