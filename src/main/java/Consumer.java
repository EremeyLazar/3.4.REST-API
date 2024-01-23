import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Consumer {

    public static void main(String[] args) {
        String URL = "http://94.198.50.185:7081/api/users";
        String URLToDelete = "http://94.198.50.185:7081/api/users/{id}";
        User userToSave = new User(3L, "James", "Brown", (byte) 33);
        User userToUpdate = new User(3L, "Thomas", "Shelby", (byte) 33);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> responseEntityEntity = template.getForEntity(URL, String.class);
//      List<String> cookies = responseEntityEntity.getHeaders().get("Set-Cookie"); // без проверки на null
        List<String> cookies = Optional.ofNullable(responseEntityEntity)
                .map(ResponseEntity::getHeaders)
                .map(headers -> headers.get("Set-Cookie"))
                .orElse(Collections.emptyList());
        System.out.println("Received Cookies: " + cookies);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Cookie", String.join("; ", cookies));

        HttpEntity<User> entity = new HttpEntity<>(userToSave, headers);
        ResponseEntity<String> responseEntityPOST = template.exchange(URL, HttpMethod.POST, entity, String.class);
        System.out.println("Response from POST: " + responseEntityPOST.getBody());

        HttpEntity<User> entity2 = new HttpEntity<>(userToUpdate, headers);
        ResponseEntity<String> responseEntityPUT = template.exchange(URL, HttpMethod.PUT, entity2, String.class);
        System.out.println("Response from PUT: " + responseEntityPUT.getBody());

        HttpEntity<User> entity3 = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntityDELETE = template.exchange(URLToDelete, HttpMethod.DELETE, entity3, String.class, 3);
        System.out.println("Response from DELETE: " + responseEntityDELETE.getBody());
    }
}
