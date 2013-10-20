package je.techtribes.domain.badge;

public interface Badge {

    public int getId();

    public String getName();

    public String getDescription();

    public int getOrder();

    public void setOrder(int order);

    public BadgeType getType();

}
