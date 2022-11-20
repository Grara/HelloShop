package pofol.shop.form.create;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateCartForm {
    private String userName;
    private Long itemId;
    private Integer count;

}
