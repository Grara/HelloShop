package pofol.shop.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class FileEntity {
    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    private String originName;
    private String saveName;
    private String savePath;
}
