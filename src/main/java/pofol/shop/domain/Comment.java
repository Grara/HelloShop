package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Comment extends BaseEntity{
    //----------필드 시작----------//
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private String createdUserName;
    private LocalDate lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;
    //----------필드 끝----------//

    //----------생성자 시작----------//
    Comment(String content, String createdUserName, Item item){
        this.content = content;
        this.createdUserName = createdUserName;
        this.item = item;
    }
    //----------생성자 끝----------//

    //----------메소드 시작----------//
    @PrePersist
    public void Setting(){
        this.lastModifiedDate = LocalDate.now();
    }
    //----------메소드 끝----------//
}
