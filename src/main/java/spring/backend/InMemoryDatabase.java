package spring.backend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring.backend.model.User;

import java.util.HashMap;
import java.util.Map;
public class InMemoryDatabase {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static InMemoryDatabase instance = null;
    public Map<String, String> users;
    private InMemoryDatabase() {
        users = new HashMap<>();
    }
    public static InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
        }
        return instance;
    }
    public void addUser(String name, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        users.put(name, hashedPassword);
    }
    public boolean validate(User user) {
        return passwordEncoder.matches(user.getPassword(),users.get(user.getUsername()));

    }
}
