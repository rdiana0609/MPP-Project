package contest.rest.services;

import contest.domain.Domain.Entities.Contest;
import contest.persistence.Repository.Interfaces.IContestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contest/contests")
public class ContestRestController {
    @Autowired
    private IContestRepository contestRepository;

    @RequestMapping(value = "/greeting", method = RequestMethod.GET)
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Contest> getAll() {
        System.out.println("Get all contests ...");
        return (List<Contest>) contestRepository.findAll();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        System.out.println("Get by id " + id);

        Contest contest = contestRepository.findOne(id);
        if (contest == null)
            return new ResponseEntity<String>("Contest not found", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Contest>(contest, HttpStatus.OK);
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(method = RequestMethod.POST)
    public Contest create(@RequestBody Contest contest) {
        System.out.println("Creating contest ...");
        Integer id = contestRepository.save(contest);
        contest.setId(id);

        return contest;
    }
    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Contest update(@RequestBody Contest contest) {
        System.out.println("Updating contest ...");
        contestRepository.update(contest);

        contest = contestRepository.findOne(contest.getId());

        return contest;

    }

    @CrossOrigin(origins = "http://localhost:5173")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        System.out.println("Deleting contest ... " + id);

        if(contestRepository.findOne(id) == null)
            return new ResponseEntity<String>("Contest not found", HttpStatus.NOT_FOUND);

        contestRepository.delete(id);
        return new ResponseEntity<Contest>(HttpStatus.OK);
    }


//    @RequestMapping("/{user}/name")
//    public String name(@PathVariable String user) {
//        User result = userRepository.findBy(user);
//        System.out.println("Result ..." + result);
//
//        return result.getName();
//    }
//
//
//    @ExceptionHandler(RepositoryException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public String userError(RepositoryException e) {
//        return e.getMessage();
//    }


}
