import contest.domain.Domain.Entities.Contest;
import contest.domain.Domain.Enums.ContestDistance;
import contest.domain.Domain.Enums.ContestAge;
import contest.rest.client.ContestRestClient;

public class StartRestClient {
    private final static ContestRestClient contestRestClient = new ContestRestClient();

    public static void main(String[] args) {
        contestRestClient.getAll().forEach(System.out::println);
        //create a new contest
        Contest contest = contestRestClient.create(new Contest(5, ContestDistance.LONG, ContestAge.ADOLESCENTI, 7));

        //get that contest
        System.out.println(contestRestClient.getById(contest.getId()));

        //update the contest with id from contest
       // contest.setDistance(ContestDistance.LONG);
        //contestRestClient.update(contest);

        //get that contest
        System.out.println(contestRestClient.getById(contest.getId()));

        //delete the contest
        contestRestClient.delete(contest.getId());

        //get all contests
        contestRestClient.getAll().forEach(System.out::println);
    }
}

