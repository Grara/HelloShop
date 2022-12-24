package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pofol.shop.domain.Comment;
import pofol.shop.domain.Item;
import pofol.shop.domain.Member;
import pofol.shop.dto.ApiResponseBody;
import pofol.shop.dto.business.CommentDto;
import pofol.shop.dto.security.UserAdapter;
import pofol.shop.form.create.CreateCommentForm;
import pofol.shop.repository.CommentRepository;
import pofol.shop.repository.ItemRepository;
import pofol.shop.repository.MemberRepository;
import pofol.shop.service.business.ItemService;
import pofol.shop.service.business.MemberService;

/**
 * 상품 후기와 관련된 API요청을 처리해주는 Controller 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-12-05
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-12-23
 */
@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    /**
     * 상품 후기를 새로 생성해줍니다.
     *
     * @param form      Comment 생성에 필요한 데이터 폼
     * @param principal 로그인 세션 정보
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-12-05
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-12-24
     */
    @PostMapping("/comments") //Item 상세페이지 Comment 생성 요청
    public ResponseEntity<ApiResponseBody<CommentDto>> createComment(@RequestBody CreateCommentForm form, @AuthenticationPrincipal UserAdapter principal) {

        Member member = memberService.findByUserName(principal.getName());

        Item item = itemService.findById(form.getItemId());
        item.addRating(form.getRating());
        itemRepository.save(item);

        Comment comment = new Comment(member, item, form.getContent(), form.getRating());
        commentRepository.save(comment);
        CommentDto dto = new CommentDto(comment);

        return ResponseEntity
                .ok()
                .body(new ApiResponseBody<>(HttpStatus.OK, "후기 생성 완료", dto));

    }
}
