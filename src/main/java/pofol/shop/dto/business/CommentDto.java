package pofol.shop.dto.business;


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
    private int rating;
    private Long profileImageId;

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.userName = comment.getMember().getUserName();
        this.lastModifiedDateTime = comment.getLastModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.rating = comment.getRating();
        this.profileImageId = comment.getMember().getProfileImage().getId();
    }

}
