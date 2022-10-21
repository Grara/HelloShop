package pofol.shop.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Comment {
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;
}
