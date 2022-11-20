package pofol.shop.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Setter(AccessLevel.NONE)
public class Item extends BaseEntity {
    //----------필드 시작----------//
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String itemName;
    private String author;
    private String isbn;
    private String description;
    private int price;
    private int quantity;

    private int totalSales;
    private int rating;

    @OneToMany(mappedBy = "item")
    private List<Comment> comments; //item에는 Comment setter프로퍼티가 없음

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileEntity thumbnailFile; //상품 이미지 경로

    //----------필드 끝----------//

    //----------Setter 시작----------//
    public void setId(Long id) {
        this.id = id;
    }

    public void setMember(Member member) { this.member = member; }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setThumbnailFile(FileEntity thumbnailFile) {
        this.thumbnailFile = thumbnailFile;
    }

    //----------Setter 끝----------//

    //----------생성자 시작----------//
    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.author = "짬뽕먹고싶다";
        this.price = price;
        this.quantity = quantity;
    }
    //----------생성자 끝----------//


    //----------메소드 시작----------//
    @PrePersist
    void beforePersist() {
        if (description == null) description = "";
    }

    public void reduceQty(int count) {
        this.quantity -= count;
    }

    public void addQty(int count) {
        this.quantity += count;
    }
    //----------메소드 끝----------//
}
