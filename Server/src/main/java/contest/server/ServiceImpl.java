package contest.server;


import contest.domain.Domain.DTOs.ParticipantDTO;
import contest.domain.Domain.Entities.Contest;
import contest.domain.Domain.Entities.Participant;
import contest.domain.Domain.Entities.Registration;
import contest.domain.Domain.Entities.User;
import contest.persistence.Repository.Interfaces.IContestRepository;
import contest.persistence.Repository.Interfaces.IParticipantRepository;
import contest.persistence.Repository.Interfaces.IRegistrationRepository;
import contest.persistence.Repository.Interfaces.IUserRepository;
import contest.services.ComException;
import contest.services.IObserver;
import contest.services.IService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceImpl implements IService {
    private IContestRepository contestRepository;
    private IParticipantRepository participantRepository;
    private IRegistrationRepository registrationRepository;
    private IUserRepository userRepository;
    private Map<String, IObserver> loggedClients;
    private final int defaultThreadsNo = 5;

    public ServiceImpl(IContestRepository contestRepository, IParticipantRepository participantRepository, IRegistrationRepository registrationRepository, IUserRepository userRepository) {
        this.contestRepository = contestRepository;
        this.participantRepository = participantRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        loggedClients = new ConcurrentHashMap<>();
    }

    public synchronized boolean login(String username, String password, IObserver client) throws ComException {
        User isRegistered = userRepository.findUser(username, password);

        if (isRegistered!=null) {
            if (loggedClients.get(username) != null)
                throw new ComException("User already logged in.");
            loggedClients.put(username, client);
        }
        if(isRegistered!=null) return true;
        else return false;
    }

    public List<Contest> getContests() {
        List<Contest> list = new ArrayList<>();
        contestRepository.findAll().forEach(contest -> {
            list.add(contest);
        });
        return list;
    }

    public List<ParticipantDTO> getParticipantsOfContest(int contestId) {
        List<Integer> participantIds = new ArrayList<>();
        registrationRepository.findByContest(contestId).forEach(registration -> {
            participantIds.add(registration.getParticipantId());
        });

        List<ParticipantDTO> participants = new ArrayList<>();

        participantIds.forEach(participantId -> {
            Participant participant = participantRepository.findOne(participantId);
            List<Integer> registeredContestIds = new ArrayList<>();
            registrationRepository.findByParticipant(participantId).forEach(registration -> {
                registeredContestIds.add(registration.getContestId());
            });

            participants.add(new ParticipantDTO(participant, registeredContestIds));
        });

        return participants;
    }

    public void addParticipant(String name, Integer age, List<Contest> selectedContests) {
        Participant participant = new Participant(0, name, age);
        Integer newId = participantRepository.save(participant);
        participant.setId(newId);

        selectedContests.forEach(contest -> {
            registrationRepository.save(new Registration(0, participant.getId(), contest.getId()));
            contest.IncrementNoOfParticipants();
            contestRepository.updateNoOfParticipants(contest);
        });

        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);

        loggedClients.forEach((username, client) -> {
            executor.execute(() -> {
                System.out.println("Notifying [" + username + "] about new participant.");
                client.participantAdded();
            });
        });

        executor.shutdown();
        // notify observers
        //loggedClients.forEach((username, client) -> client.participantAdded());
    }

    @Override
    public void logout(String username) throws ComException {
        IObserver localClient = loggedClients.remove(username);
        if (localClient == null)
            throw new ComException("User " + username + " is not logged in.");
    }
}
