package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 주문시트에 대한 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-10
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Entity
@Data
@NoArgsConstructor
public class OrderSheet {
    @Id @GeneratedValue
    @Column(name = "ordersheet_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //단방향 다대일
    @JoinColumn(name = "member_id")
    private Member member;

    @Lob
    private String content;
    private Boolean isOrdered; //이 주문시트로 주문이 되었는가 여부
}
