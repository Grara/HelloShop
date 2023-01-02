package pofol.shop.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;

@RedisHash("test")
@NoArgsConstructor
@Data
public class TestEntity implements Serializable {

    @Id
    private Long id;
    private String name;

    public TestEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}