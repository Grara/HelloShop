package pofol.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pofol.shop.exception.NotEnoughQuantityException;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Item extends BaseEntity {
    //----------필드 시작----------//
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "member_id")
    private Member member; //아이템을 등록한 회원

    @OneToMany(mappedBy = "item") //양방향 일대다
    @Setter(AccessLevel.NONE)//Comment가 양방향 관계의 주인이므로 Setter 없앰
    private List<Comment> comments;

    @OneToOne(fetch = FetchType.LAZY) //단방향 일대일
    @JoinColumn(name = "file_id")
    private FileEntity thumbnailFile; //상품 이미지 파일 엔티티

    private String itemName;
    private String author;
    private String isbn;
    private String description;
    private int price;
    private int quantity;
    private int totalSales;
    private int rating;
    //----------필드 끝 / 생성자 시작----------//
    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.author = "짬뽕먹고싶다";
        this.price = price;
        this.quantity = quantity;
    }

    public Item(Member member, String itemName, int price, int quantity){
        this.member = member;
        this.itemName = itemName;
        this.author = "흥엉이";
        this.price = price;
        this.quantity = quantity;
    }
    //----------생성자 끝 / 메소드 시작----------//
    @PrePersist
    void beforePersist() {
        if (description == null) description = "";
    }

    public void reduceQty(int count) throws NotEnoughQuantityException {
        if(this.quantity < count) throw new NotEnoughQuantityException("현재 재고가 부족합니다.");
        else this.quantity -= count;
    }

    public void addQty(int count) {
        this.quantity += count;
    }
}
