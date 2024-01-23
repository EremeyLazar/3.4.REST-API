import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class Consumer {

    public static void main(String[] args) {
        String URL = "http://94.198.50.185:7081/api/users";
        String URLdelete = "http://94.198.50.185:7081/api/users/{id}";

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> forEntity = template.getForEntity(URL, String.class);
        List<String> cookies = forEntity.getHeaders().get("Set-Cookie");

        System.out.println("Received Cookies: " + cookies);

        User userToSave = new User(3L, "James", "Brown", (byte) 33);
        User userToChange = new User(3L, "Thomas", "Shelby", (byte) 33);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", String.join("; ", cookies));

        HttpEntity<User> entity = new HttpEntity<>(userToSave, headers);
        ResponseEntity<String> responseEntity1 = template.exchange(URL, HttpMethod.POST, entity, String.class);
        System.out.println("Response from POST: " + responseEntity1.getBody());

        HttpEntity<User> entity2 = new HttpEntity<>(userToChange, headers);
        ResponseEntity<String> responseEntity2 = template.exchange(URL, HttpMethod.PUT, entity2, String.class);
        System.out.println("Response from PUT: " + responseEntity2.getBody());

        HttpEntity<User> entity3 = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity3 = template.exchange(URLdelete, HttpMethod.DELETE, entity3, String.class, 3);
        System.out.println("Response from DELETE: " + responseEntity3.getBody());
    }
}
