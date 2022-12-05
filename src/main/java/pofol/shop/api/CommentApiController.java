package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.dto.CommentDto;
import pofol.shop.form.create.CreateCommentForm;
import pofol.shop.repository.CommentRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    @PostMapping("/comments/new") //Item 상세페이지 Comment 생성 요청
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentForm form, Principal principal) {
        System.out.println(form.getContent());
        Member member = memberRepository.findByUserName(principal.getName()).orElseThrow();
        Item item = itemRepository.findById(form.getItemId()).orElseThrow();
        Comment comment = new Comment(member, item, form.getContent(), form.getRating());
        commentRepository.save(comment);
        CommentDto dto = new CommentDto(comment);
        return ResponseEntity
                .ok()
                .body(dto);
    }
}
