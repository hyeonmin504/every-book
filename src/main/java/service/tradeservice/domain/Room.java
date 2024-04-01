package service.tradeservice.domain;

import jakarta.persistence.*;
import service.tradeservice.domain.user.User;

import java.time.LocalDateTime;

@Entity
public class Room {

    @Id @GeneratedValue
    @Column(name = "room_id")
    private Long id;

    private LocalDateTime createDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;



}
