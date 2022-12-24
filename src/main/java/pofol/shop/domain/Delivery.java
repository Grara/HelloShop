package pofol.shop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.enums.DeliveryStatus;

import javax.persistence.*;

/**
 * 배송 정보를 나타내는 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-13
 */
@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String receiverName; //받는이
    private String receiverPhoneNumber; //받는분 연락처
    private String memo; //배송메모

    @Embedded
    private Address address; //주소

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //배송상태
    //----------필드 끝 / 생성자 시작----------//
    @Builder
    public Delivery(String receiverName, String receiverPhoneNumber, String memo, Address address) {
        this.receiverName = receiverName;
        this.receiverPhoneNumber = receiverPhoneNumber;
        this.memo = memo;
        this.address = address;
        this.status = DeliveryStatus.READY;
    }
}
