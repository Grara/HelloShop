package pofol.shop.domain.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * 주소정보를 나타내는 임베디드 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-21
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-10-23
 */
@Embeddable
@Data
@NoArgsConstructor
public class Address {
    private String address1;
    private String address2;
    @Column(nullable = true)
    private int zipcode;

    public Address(String address1, String address2, int zipcode) {
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }
}
