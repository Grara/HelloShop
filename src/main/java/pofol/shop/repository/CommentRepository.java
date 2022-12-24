package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;

import java.util.List;

/**
 * 스프링데이터JPA를 사용한 상품 후기 관련 DAO입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-10-24
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-06
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Item에 달린 Comment들을 찾고 각 Comment의 Member와 해당 Member의 프로파일이미지 파일의 FileEntity를 페치조인합니다.
     *
     * @param item 찾을 Comment들이 달린 Item
     * @return 찾아낸 Comment List
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-10-24
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-06
     */
    @Query("select c from Comment c left join fetch c.member m left join fetch m.profileImage where c.item = :item")
    List<Comment> findListByItem(@Param("item") Item item);

}
