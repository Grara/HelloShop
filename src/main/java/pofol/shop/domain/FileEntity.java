package pofol.shop.domain;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * 이미지, 파일에 대한 메타데이터를 저장한 엔티티 클래스입니다.
 *
 * @createdBy : 노민준(nomj18@gmail.com)
 * @createdDate : 2022-11-19
 * @lastModifiedBy : 노민준(nomj18@gmail.com)
 * @lastModifiedDate : 2022-11-19
 */
@Entity
@Data
@NoArgsConstructor
public class FileEntity {
    @Id @GeneratedValue
    @Column(name = "file_id")
    private Long id;
    private String originName; //파일을 업로드할 당시의 파일명
    private String saveName; //확장자 포함한 저장 파일명
    private String savePath; //경로 + saveName
}
