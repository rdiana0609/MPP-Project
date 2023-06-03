package contest.networking.dtos;

import contest.domain.Domain.Entities.Contest;

import java.io.Serializable;
import java.util.List;

public class RegistrationDTO implements Serializable {
    private String name;
    private Integer age;
    private List<Contest> selectedContests;

    public RegistrationDTO(String name, Integer age, List<Contest> selectedContests) {
        this.name = name;
        this.age = age;
        this.selectedContests = selectedContests;
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

    public List<Contest> getSelectedContests() {
        return selectedContests;
    }

    public void setSelectedContests(List<Contest> selectedContests) {
        this.selectedContests = selectedContests;
    }
}
