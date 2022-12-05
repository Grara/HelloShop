package pofol.shop.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Comment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
public class CommentDto {
    private String content;
    private String userName;
    private String lastModifiedDateTime;

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.userName = comment.getMember().getUserName();
        this.lastModifiedDateTime = comment.getLastModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

}
