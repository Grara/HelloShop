package pofol.shop.formAndDto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartCreateForm {
    private String userName;
    private Long itemId;
    private Integer count;

}
