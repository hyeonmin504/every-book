package service.tradeservice.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    @Id @GeneratedValue
    @Column(name = "content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    private String sendUser;

    private String sendDate;

    private String content;

    public Content(String nickName,Room room, String content) {
        this.sendUser = nickName;
        this.room = room;
        this.sendDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss"));
        this.content = content;
    }

    public Content(String content) {
        this.content = content;
    }

    protected void setRoom(Room room) {
        this.room = room;
    }
}
