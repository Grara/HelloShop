package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Comment extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;
    private String content;
    private String createdUserName;
    private LocalDate lastModifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @PrePersist
    public void Setting(){
        this.lastModifiedDate = LocalDate.now();
    }

    Comment(String content, String createdUserName, Item item){
        this.content = content;
        this.createdUserName = createdUserName;
        this.item = item;
    }
}
