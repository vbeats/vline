package com.codestepfish.vlineex.test;

import com.alibaba.fastjson2.JSONObject;
import com.codestepfish.vlineex.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api")
public class TestController {

    @PostMapping("/test")
    public R test(@RequestBody JSONObject param) {
        log.info("================+> rec : {}", param);

        return new R(200, "success");
    }
}