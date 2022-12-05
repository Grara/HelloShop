package pofol.shop.form.create;

import lombok.Data;

@Data
public class CreateCommentForm {
    private Long itemId;
    private String content;
}
