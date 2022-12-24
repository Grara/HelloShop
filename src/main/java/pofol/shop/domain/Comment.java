package pofol.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 상품 후기를 나타내는 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-05
 */
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
    private int rating;
    private LocalDateTime lastModifiedDateTime;

    //----------필드 끝 / Setter 시작----------//
    public void setItem(Item item){
        //양방향 연관 관계에서 주인이기 때문에 필요한 setter
        this.item = item;
        item.getComments().add(this);
    }
    //----------Setter 끝 / 생성자 시작----------//
    public Comment(Member member, Item item, String content, int rating){
        this.content = content;
        this.member = member;
        this.item = item;
        this.rating = rating;
    }
    //----------생성자 끝 / 메소드 시작----------//

    /**
     * DB에 저장하기전에 마지막 수정시간을 현재로 기록합니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-19
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-13
     */
    @PrePersist
    public void prePersist(){
        this.lastModifiedDateTime = LocalDateTime.now();
    }
}
