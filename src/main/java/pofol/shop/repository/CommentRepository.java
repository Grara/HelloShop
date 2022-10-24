package pofol.shop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findListByItem(Item item);
}
