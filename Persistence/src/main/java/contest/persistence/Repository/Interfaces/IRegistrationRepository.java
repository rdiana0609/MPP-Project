package contest.persistence.Repository.Interfaces;


import contest.domain.Domain.Entities.Registration;

public interface IRegistrationRepository extends IRepository<Integer, Registration>{
    Iterable<Registration> findByContest(Integer idContest);
    Iterable<Registration> findByParticipant(Integer idParticipant);
}
