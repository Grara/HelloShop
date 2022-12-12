package pofol.shop.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.embedded.Address;
import pofol.shop.domain.enums.DeliveryStatus;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    private String receiverName;
    private String receiverPhoneNumber;
    private String memo;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
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
