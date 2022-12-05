package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Comment extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //양방향 다대일
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    private Member member; //후기 작성자

    private String content;
    private LocalDateTime lastModifiedDateTime;

    //----------필드 끝 / Setter 시작----------//
    public void setItem(Item item){
        //양방향 연관 관계에서 주인이기 때문에 필요한 setter
        this.item = item;
        item.getComments().add(this);
    }
    //----------Setter 끝 / 생성자 시작----------//
    public Comment(String content, Member member, Item item){
        this.content = content;
        this.member = member;
        this.item = item;
    }
    //----------생성자 끝 / 메소드 시작----------//
    @PrePersist
    public void Setting(){
        this.lastModifiedDateTime = LocalDateTime.now();
    }
}
