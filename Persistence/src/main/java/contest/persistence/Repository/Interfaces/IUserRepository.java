package contest.persistence.Repository.Interfaces;


import contest.domain.Domain.Entities.User;

public interface IUserRepository extends IRepository<String, User> {
    User findUser(String username, String password);
}
