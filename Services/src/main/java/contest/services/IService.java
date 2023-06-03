package contest.services;

import contest.domain.Domain.Entities.Contest;
import contest.domain.Domain.DTOs.ParticipantDTO;

import java.util.List;

public interface IService {
        boolean login(String username, String password, IObserver client) throws ComException;
        List<Contest> getContests() throws ComException;
        List<ParticipantDTO> getParticipantsOfContest(int contestId) throws ComException;
        void addParticipant(String name, Integer age, List<Contest> selectedContests) throws ComException;
        void logout(String username) throws ComException;
}
