# Fixes to apply live during class

## 1) Secret scanning fix

Open `src/main/resources/application.properties` and delete this line:

```properties
demo.aws.access.key=REDACTED_DEMO_KEY
```

Explain that secrets should come from environment variables or a secret manager.

---

## 2) SQL injection fix

Replace the `search()` method with:

```java
@GetMapping("/search")
public List<Map<String, Object>> search(@RequestParam Integer id) {
    return jdbcTemplate.queryForList(
            "SELECT * FROM users WHERE id = ?",
            id
    );
}
```

---

## 3) Reflected XSS fix

Add this import:

```java
import org.springframework.web.util.HtmlUtils;
```

Replace the `echo()` method with:

```java
@GetMapping(value = "/echo", produces = "text/plain")
public String echo(@RequestParam String msg) {
    return HtmlUtils.htmlEscape(msg);
}
```

---

## 4) Vulnerable dependency fix

Open `pom.xml` and remove this dependency entirely for the final green run:

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.14.1</version>
</dependency>
```

For the demo, removing it is simpler than explaining a version upgrade.
