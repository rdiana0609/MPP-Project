package contest.rest.client;

import contest.domain.Domain.Entities.Contest;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Callable;

public class ContestRestClient {
    private static final String URL = "http://localhost:8080/contest/contests";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable){
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Contest> getAll() {
        return List.of(execute(() -> restTemplate.getForObject(URL, Contest[].class)));
    }

    public Contest getById(Integer id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Contest.class));
    }

    public Contest create(Contest contest) {
        return execute(() -> restTemplate.postForObject(URL, contest, Contest.class));
    }

    public void update(Contest contest) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, contest.getId()), contest);
            return null;
        });
    }

    public void delete(Integer id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
}
