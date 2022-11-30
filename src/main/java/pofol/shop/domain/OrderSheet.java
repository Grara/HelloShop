package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
