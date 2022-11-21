package pofol.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pofol.shop.domain.FileEntity;
import pofol.shop.repository.FileRepository;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    @Value("${fileDir}")
    private String FILE_DIR;

    public Long saveFile(MultipartFile uploadFile) throws Exception {
        if(uploadFile.getOriginalFilename().equals("")){
            throw new IllegalArgumentException("이미지 첨부 안함 or 파일 이름이 없음");
        }
        FileEntity file = new FileEntity();
        file.setOriginName(uploadFile.getOriginalFilename());
        String uuid = UUID.randomUUID().toString();
        String extention = file.getOriginName().substring(file.getOriginName().lastIndexOf("."));
        file.setSaveName(uuid + extention);
        file.setSavePath(FILE_DIR + file.getOriginName());
        uploadFile.transferTo(new File(file.getSavePath()));
        fileRepository.save(file);
        return file.getId();
    }

    public FileEntity findOne(Long id) throws Exception{
        return fileRepository.findById(id).orElseThrow(()->new EntityNotFoundException("fileEntity not found"));
    }

    public Long initFile(FileEntity file) throws Exception{
        FileEntity saveFile = fileRepository.save(file);
        return saveFile.getId();
    }
}
