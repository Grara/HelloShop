package pofol.shop.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.TestEntity;
import pofol.shop.repository.RedisRepository;

@RestController
@RequiredArgsConstructor
public class TestApiController {

    private final RedisRepository redisRepository;

    @PostMapping("/test")
    public String test(@RequestBody TestEntity test) {
        return redisRepository.save(test).toString();
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return redisRepository.findAll().toString();
    }
}
