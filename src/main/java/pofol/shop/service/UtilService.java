package pofol.shop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UtilService {
    @Value("${pagingInterval}")
    private int pagingInterval;

    public void pagingCommonTask(Page page, Model model){
        //페이지 이동 버튼의 시작 페이지 번호
        //1~10, 11~20 ··· 이런식으로 UI에 보여짐
        int pageStart = page.getNumber() / pagingInterval * pagingInterval + 1;

        //페이지 이동 버튼 끝 페이지 번호, 전체 페이지 중 마지막 페이지까지만
        int pageEnd = Math.min(pageStart + pagingInterval - 1, page.getTotalPages());
        if (pageEnd <= 0) pageEnd = 1;

        model.addAttribute("totalPage", page.getTotalPages());
        model.addAttribute("pageStart", pageStart);
        model.addAttribute("pageEnd", pageEnd);
        model.addAttribute("curNumber", page.getNumber() + 1); //현재 페이지 번호
    }
}
