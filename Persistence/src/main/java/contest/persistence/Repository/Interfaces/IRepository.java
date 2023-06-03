package contest.persistence.Repository.Interfaces;


import contest.domain.Domain.Entity;

public interface IRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);

    Iterable<E> findAll();

    ID save(E entity);

    void delete(ID id);

    void update(E entity);
}
