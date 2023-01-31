package pofol.shop.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pofol.shop.domain.TestEntity;
import pofol.shop.repository.RedisRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("/kkotest")
    public HashMap<String, Object> callkko(@RequestBody Map<String, Object> params){
        HashMap<String, Object> resultJson = new HashMap<>();

        try{

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(params);
            System.out.println(jsonInString);

            List<HashMap<String,Object>> outputs = new ArrayList<>();
            HashMap<String,Object> template = new HashMap<>();
            HashMap<String, Object> simpleText = new HashMap<>();
            HashMap<String, Object> text = new HashMap<>();

            text.put("text","코딩32 발화리턴입니다.");
            simpleText.put("simpleText",text);
            outputs.add(simpleText);

            template.put("outputs",outputs);

            resultJson.put("version","2.0");
            resultJson.put("template",template);

        }catch (Exception e){

        }

        return resultJson;
    }
}
