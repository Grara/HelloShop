package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Item에 달린 Comment들을 찾고 각 Comment의 Member와 해당 Member의 프로파일이미지 파일의 FileEntity를 페치조인합니다.
     * @param item 찾을 Comment들이 달린 Item
     * @return 찾아낸 Comment List
     */
    @Query("select c from Comment c left join fetch c.member m left join fetch m.profileImage where c.item = :item")
    List<Comment> findListByItem(@Param("item") Item item);

}
