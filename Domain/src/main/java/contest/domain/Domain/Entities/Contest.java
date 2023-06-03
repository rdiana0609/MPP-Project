package contest.domain.Domain.Entities;

import contest.domain.Domain.Entity;
import contest.domain.Domain.Enums.ContestDistance;
import contest.domain.Domain.Enums.ContestAge;

public class Contest extends Entity<Integer> {
    private ContestDistance distance;
    private ContestAge style;
    private Integer noOfParticipants;
    public Contest(){}
    public Contest(Integer integer, ContestDistance distance, ContestAge style, Integer noOfParticipants) {
        super(integer);
        this.distance = distance;
        this.style = style;
        this.noOfParticipants = noOfParticipants;
    }


    public void IncrementNoOfParticipants() {
        this.noOfParticipants++;
    }

    public ContestDistance getDistance() {
        return distance;
    }

    public void setDistance(ContestDistance distance) {
        this.distance = distance;
    }

    public ContestAge getStyle() {
        return style;
    }

    public void setStyle(ContestAge style) {
        this.style = style;
    }

    public Integer getNoOfParticipants() {
        return noOfParticipants;
    }

    public void setNoOfParticipants(Integer noOfParticipants) {
        this.noOfParticipants = noOfParticipants;
    }

    @Override
    public String toString() {
        return "Contest{" +
                "distance=" + distance +
                ", age=" + style +
                ", noOfParticipants=" + noOfParticipants +
                '}';
    }
}

