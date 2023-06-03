package contest.persistence.Repository.Interfaces;


import contest.domain.Domain.Entities.Contest;

public interface IContestRepository extends IRepository<Integer, Contest>{
    public void updateNoOfParticipants(Contest contest);
}
