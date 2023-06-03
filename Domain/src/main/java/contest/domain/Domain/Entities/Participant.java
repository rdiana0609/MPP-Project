package contest.domain.Domain.Entities;


import contest.domain.Domain.Entity;

public class Participant extends Entity<Integer> {
    String name;
    Integer age;

    public Participant(Integer integer, String name, Integer age) {
        super(integer);
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
