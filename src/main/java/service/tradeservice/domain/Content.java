package service.tradeservice.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Content {

    @Id @GeneratedValue
    @Column(name = "content_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Room room;

    private LocalDateTime sendDate;

    private String content;

}
