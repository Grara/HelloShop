package pofol.shop.controller.form;

import lombok.Data;

@Data
public class CommentForm {
    private Long itemId;
    private String createdUserName;
    private String content;
}
