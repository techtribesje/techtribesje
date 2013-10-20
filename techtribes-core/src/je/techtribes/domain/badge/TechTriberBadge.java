package je.techtribes.domain.badge;

public class TechTriberBadge extends AbstractBadge {

    public TechTriberBadge() {
        super(Badges.TECH_TRIBER_ID, "Tech Triber", "Awarded to anybody that helps with Tech Tribes");
    }

    @Override
    public BadgeType getType() {
        return BadgeType.Person;
    }

}
