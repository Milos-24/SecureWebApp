package spring.backend.login;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.backend.InMemoryDatabase;
import spring.backend.model.User;

@RestController
public class LoginController {
    InMemoryDatabase db = InMemoryDatabase.getInstance();

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody User user) {
        return ResponseEntity.ok(db.validate(user));
    }

}