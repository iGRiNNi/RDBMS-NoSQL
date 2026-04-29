package dto;

public class ClusterHierarchyDto {
    private final long id;
    private final String name;
    private final int level;

    public ClusterHierarchyDto(long id, String name, int level) {
        this.id = id;
        this.name = name;
        this.level = level;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }
}
