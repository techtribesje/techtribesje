package je.techtribes.domain;

public enum TalkType {

    Presentation,
    Keynote,
    Workshop,
    Panel;

    public static TalkType lookupByChar(char type) {
        switch (type) {
            case 'k':
                return Keynote;
            case 'w':
                return Workshop;
            case 'p':
                return Panel;
            default:
                return Presentation;
        }
    }

}
