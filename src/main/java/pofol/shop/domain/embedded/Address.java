package pofol.shop.domain.embedded;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class Address {
    private String address1;
    private String address2;
    private int zipcode;

    public Address(String address1, String address2, int zipcode) {
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }
}
