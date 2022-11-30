package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.FileEntity;
import pofol.shop.form.update.UpdateImageForm;
import pofol.shop.repository.FileRepository;
import pofol.shop.service.FileService;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileRepository fileRepository;
    @GetMapping("/images/{fileId}") //html img태그들의 src경로
    @ResponseBody
    public Resource downloadImage(@PathVariable("fileId") Long id, Model model) throws Exception {

        FileEntity file = fileRepository.findById(id).orElseThrow();
        UrlResource url = new UrlResource("file:" + file.getSavePath());

        return url;
    }

}
