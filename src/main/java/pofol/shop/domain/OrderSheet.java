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
    @Lob
    private String content;
    private Boolean isOrdered;
    private Long test;
}
