package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 특정 Item에 달린 Comment들을 찾습니다.
     * @param item 찾을 Comment들이 달린 Item
     * @return 찾아낸 Comment List
     */
    List<Comment> findListByItem(Item item);
}
