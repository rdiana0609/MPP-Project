package contest.domain.Domain.Enums;

public enum ContestDistance {
    SHORT("50m"),
    MEDIUM("100m"),
    LONG("800m"),
    LONG2("1500m");

    public String length;

    ContestDistance(String length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return length;
    }

    public static ContestDistance getByLength(String length) {
        for (ContestDistance distance : ContestDistance.values()) {
            if (distance.length.equals(length)) {
                return distance;
            }
        }
        throw new IllegalArgumentException("No matching ContestDistance found for length: " + length);
    }

}
