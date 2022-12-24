package pofol.shop.domain;

import lombok.*;
import pofol.shop.exception.NotEnoughQuantityException;

import javax.persistence.*;
import java.util.List;

/**
 * 상품에 대한 정보를 나타내는 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
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
    private String descriptionTitle;
    private String description;

    private int price;
    private int quantity;

    @Setter(AccessLevel.NONE)
    private int totalSales;

    @Setter(AccessLevel.NONE)
    private int totalRating;

    @Setter(AccessLevel.NONE)
    private float ratingAverage;

    @Setter(AccessLevel.NONE)
    private int ratingUserCount;

    //----------필드 끝 / 생성자 시작----------//
    public Item(String itemName, int price, int quantity) {
        this.itemName = itemName;
        this.author = "짬뽕먹고싶다";
        this.price = price;
        this.quantity = quantity;
    }

    public Item(Member member, String itemName, int price, int quantity) {
        this.member = member;
        this.itemName = itemName;
        this.author = "흥엉이";
        this.price = price;
        this.quantity = quantity;
    }
    @Builder
    public Item(Member member, FileEntity thumbnailFile, String itemName, String author, String descriptionTitle, String description, int price, int quantity) {
        this.member = member;
        this.thumbnailFile = thumbnailFile;
        this.itemName = itemName;
        this.author = author;
        this.descriptionTitle = descriptionTitle;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    //----------생성자 끝 / 메소드 시작----------//

    /**
     * 저장하기전에 설명 제목과 본문이 비었다면 기본 설정을 해줍니다.
     *
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-07
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-07
     */
    @PrePersist
    void prePersist() {
        if (this.descriptionTitle == null) this.descriptionTitle ="제목없음";
        if (this.description == null) this.description = "설명을 입력해주세요";
    }

    /**
     * 재고를 감소시킵니다.
     *
     * @param count 감소시킬 수량
     * @throws NotEnoughQuantityException 재고가 부족할 경우 발생합니다.
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-07
     */
    public void reduceQty(int count) throws NotEnoughQuantityException {
        if (this.quantity < count) throw new NotEnoughQuantityException("현재 재고가 부족합니다.");
        else {
            this.quantity -= count;
            this.totalSales += count;
        }
    }

    /**
     * 재고를 증가시킵니다.
     *
     * @param count 증가시킬 수량
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-21
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-10-21
     */
    public void addQty(int count) {
        this.quantity += count;
    }

    /**
     * 총 평점을 추가합니다.
     *
     * @param score 증가시킬 평점
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-07
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-07
     */
    public void addRating(int score) { //평점 추가
        this.totalRating += score; //총합 추가
        this.ratingUserCount++; //참여자수 1 증가
        float temp = (float) totalRating / ratingUserCount; //평점 평균
        this.ratingAverage = Math.round(temp * 10) / 10.0f; //평점 평균을 1자리까지만
    }
}
