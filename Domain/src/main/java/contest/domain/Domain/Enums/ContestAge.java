package contest.domain.Domain.Enums;

public enum ContestAge {
    COPII("7-14 ani"),
    ADOLESCENTI("12-18 ani"),
    ADULTI("18-70 ani"),
    MIXED("35-80 ani");
    public String type;
    ContestAge(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ContestAge getByType(String type) {
        for (ContestAge distance : ContestAge.values()) {
            if (distance.type.equals(type)) {
                return distance;
            }
        }
        throw new IllegalArgumentException("No matching ContestDistance found for length: " + type);
    }
}
