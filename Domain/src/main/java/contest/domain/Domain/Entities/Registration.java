package contest.domain.Domain.Entities;


import contest.domain.Domain.Entity;

public class Registration extends Entity<Integer> {
    Integer participantId;
    Integer contestId;

    public Registration(Integer integer, Integer participantId, Integer contestId) {
        super(integer);
        this.participantId = participantId;
        this.contestId = contestId;
    }

    public Integer getParticipantId() {
        return participantId;
    }

    public void setParticipantId(Integer participantId) {
        this.participantId = participantId;
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }
}
