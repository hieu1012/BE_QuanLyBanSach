package iuh.fit.entities.enums;

public enum Role {
    MASTER(3, "Quản trị cao nhất"),
    ADMIN(2, "Quản trị viên"),
    USER(1, "Người dùng");

    private final int level;
    private final String description;

    Role(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public int getLevel() {
        return level;
    }

    public String getDescription() {
        return description;
    }

    public boolean hasHigherAuthorityThan(Role other) {
        return this.level > other.level;
    }

    public boolean canManage(Role other) {
        return this.level > other.level;
    }
}