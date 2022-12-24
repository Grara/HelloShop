package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.FileEntity;
import pofol.shop.repository.FileRepository;
import pofol.shop.service.FileService;

/**
 * 사이트에 표시할 img태그들의 src경로로 이미지를 보내주는 역할의 Controller입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-19
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-30
 */
@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    private final FileService fileService;

    /**
     * id에 해당하는 이미지파일을 보내줍니다.
     *
     * @param id db저장된 이미지파일의 FileEntity id값
     * @createdBy : 노민준(nomj18@gmail.com)
     * @createdDate : 2022-11-19
     * @lastModifiedBy : 노민준(nomj18@gmail.com)
     * @lastModifiedDate : 2022-11-30
     */
    @GetMapping("/images/{fileId}") //html img태그들의 src경로
    @ResponseBody
    public Resource downloadImage(@PathVariable("fileId") Long id, Model model) throws Exception {

        FileEntity file = fileService.findById(id);
        UrlResource url = new UrlResource("file:" + file.getSavePath());

        return url;
    }

}
