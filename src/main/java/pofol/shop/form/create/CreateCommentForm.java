package pofol.shop.form.create;

import lombok.Data;

@Data
public class CreateCommentForm {
    private Long itemId;
    private String createdUserName;
    private String content;
}
