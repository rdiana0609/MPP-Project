package contest.domain.Domain.DTOs;


import contest.domain.Domain.Entities.Participant;
import contest.domain.Domain.Entity;

import java.util.List;

public class ParticipantDTO extends Entity<Integer> {
    private String name;
    private Integer age;
    private String registeredContestIds;

    public ParticipantDTO(Participant participant, List<Integer> registeredContestIds) {
        super(participant.getId());
        this.name = participant.getName();
        this.age = participant.getAge();

        this.registeredContestIds = "";
        for (Integer registeredContestId : registeredContestIds) {
            this.registeredContestIds += registeredContestId + ", ";
        }

        this.registeredContestIds = this.registeredContestIds.substring(0, this.registeredContestIds.length() - 2);
    }

    public String getRegisteredContestIds() {
        return registeredContestIds;
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
