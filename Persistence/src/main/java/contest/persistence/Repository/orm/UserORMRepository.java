package contest.persistence.Repository.orm;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import contest.domain.Domain.Entities.User;
import contest.persistence.Repository.Interfaces.IUserRepository;


public class UserORMRepository implements IUserRepository {
    private SessionFactory sessionFactory;

    public UserORMRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public User findOne(String s) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from User u where u.username = ?1";
                return session.createQuery(queryString, User.class)
                        .setParameter(1, s)
                        .setMaxResults(1).uniqueResult();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                return session.createQuery("from User", User.class).list();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }

    @Override
    public String save(User entity) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                session.save(entity);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return entity.getUsername();
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void update(User entity) {

    }

    @Override
    public User findUser(String username, String password) {
        try(Session session = sessionFactory.openSession()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                String queryString = "from User e where e.username = ?1 and e.password = ?2";
                return session.createQuery(queryString, User.class)
                        .setParameter(1, username)
                        .setParameter(2, password)
                        .setMaxResults(1).uniqueResult();
            } catch (Exception e) {
                if (transaction != null) {
                    transaction.rollback();
                }
            }
        }
        return null;
    }
}
