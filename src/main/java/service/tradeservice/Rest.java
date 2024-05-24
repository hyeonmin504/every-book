package service.tradeservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Rest {

    @GetMapping("/hell")
    public String hell () {
        return "ok";
    }
}
