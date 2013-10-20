package je.techtribes.domain.badge;

abstract class AbstractBadge implements Badge {

    private int id;
    private String name;
    private String description;

    private int order;

    protected AbstractBadge(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public BadgeType getType() {
        return BadgeType.PersonAndTribe;
    }

}
