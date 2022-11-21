package pofol.shop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import pofol.shop.domain.FileEntity;
import pofol.shop.service.FileService;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/images/{fileId}")
    @ResponseBody
    public Resource downloadImage(@PathVariable("fileId")Long id, Model model) throws Exception {
        FileEntity file = fileService.findOne(id);
        System.out.println("$$$$$$$" + file.getSavePath());
        UrlResource url = new UrlResource("file:" + file.getSavePath());
        System.out.println("#####" + url);
        return url;
    }
}
