package pofol.shop.dto.business;


import lombok.Data;
import lombok.NoArgsConstructor;
import pofol.shop.domain.Comment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 상품 후기의 DTO클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-05
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-05
 */
@Data
@NoArgsConstructor
public class CommentDto {
    private String content; //내용
    private String userName; //작성자명
    private String lastModifiedDateTime; //마지막 수정일
    private int rating; //평점
    private Long profileImageId; //작성자 프로필 이미지의 FIleEntity id

    public CommentDto(Comment comment){
        this.content = comment.getContent();
        this.userName = comment.getMember().getUserName();
        this.lastModifiedDateTime = comment.getLastModifiedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        this.rating = comment.getRating();
        this.profileImageId = comment.getMember().getProfileImage().getId();
    }

}
