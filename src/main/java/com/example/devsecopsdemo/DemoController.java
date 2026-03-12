package com.example.devsecopsdemo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    private final JdbcTemplate jdbcTemplate;

    public DemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/")
    public String home() {
        return "DevSecOps demo is running. Try /search?id=1 and /echo?msg=hello";
    }

    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam Integer id) {
        return jdbcTemplate.queryForList(
                "SELECT * FROM users WHERE id = ?",
                id
        );
    }

    @GetMapping(value = "/echo", produces = "text/plain")
    public String echo(@RequestParam String msg) {
        return HtmlUtils.htmlEscape(msg);
    }
}
