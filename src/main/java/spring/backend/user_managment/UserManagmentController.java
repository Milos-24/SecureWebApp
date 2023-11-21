package spring.backend.user_managment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import spring.backend.InMemoryDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
public class UserManagmentController {
    private final UserManagmentService userManagmentService;
    InMemoryDatabase db = InMemoryDatabase.getInstance();
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    public UserManagmentController(UserManagmentService userManagmentService) {
        this.userManagmentService = userManagmentService;
    }

    @GetMapping("/users")
    public List<String> getUsers(@RequestParam String loggedUser)
    {
        return new ArrayList<>(db.users.keySet())
                .stream()
                .filter(user-> !user.equals(loggedUser))
                .collect(Collectors.toList());
    }

    @PostMapping("/users")
    public ResponseEntity<Boolean> sendMessage(@RequestBody String rawMessage)
    {
        System.out.println("Backend:"+rawMessage);

        String server1= "https://localhost:443/save";
        String server2= "https://localhost:8444/save";
        String server3= "https://localhost:8445/save";
        String server4= "https://localhost:8446/save";
        String[] servers = {server1, server2, server3, server4};

        Random random = new Random();
        int randomIndex = random.nextInt(servers.length);
        String selectedServer = servers[randomIndex];

        HttpEntity<String> request = new HttpEntity<>(rawMessage+"@");
        ResponseEntity<String> response = restTemplate.postForEntity(selectedServer, request, String.class);

        return ResponseEntity.ok(true);

    }

    @GetMapping("/messages")
    public List<String> getMessages(@RequestParam String loggedUser)
    {
        //serveri
        String server1Get= "https://localhost:443/get";
        String server2Get= "https://localhost:8444/get";
        String server3Get= "https://localhost:8445/get";
        String server4Get= "https://localhost:8446/get";

        //dobavi sve poruke sa svih servera pri cemu ce serveri vratiti samo za poslanog usera

        String server1Post= "https://localhost:443/save";
        String server2Post= "https://localhost:8444/save";
        String server3Post= "https://localhost:8445/save";
        String server4Post= "https://localhost:8446/save";


        String response1 = restTemplate.getForObject(server1Get,String.class);
        String response2 = restTemplate.getForObject(server2Get,String.class);
        String response3 = restTemplate.getForObject(server3Get,String.class);
        String response4 = restTemplate.getForObject(server4Get, String.class);

        String message1 = userManagmentService.filterServer(response1, loggedUser,server1Post);
        String message2 = userManagmentService.filterServer(response2, loggedUser,server2Post);
        String message3 = userManagmentService.filterServer(response3, loggedUser,server3Post);
        String message4 = userManagmentService.filterServer(response4, loggedUser,server4Post);

        String messages = message1+message2+message3+message4;
        String sortedMessages = String.valueOf(userManagmentService.filterMessage(messages));

        if(sortedMessages.isEmpty())
        {
            return new ArrayList<String>();
        }

        return Collections.singletonList(sortedMessages);

    }

}
