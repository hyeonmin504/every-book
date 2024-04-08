package service.tradeservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Content {

    @Id @GeneratedValue
    @Column(name = "content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Room room;

    private Long sendUser;

    private LocalDateTime sendDate;

    private String content;

    public Content(Long userId,Room room, String content) {
        this.sendUser = userId;
        this.room = room;
        this.sendDate = LocalDateTime.now();
        this.content = content;
    }

    protected void setRoom(Room room) {
        this.room = room;
    }
}
