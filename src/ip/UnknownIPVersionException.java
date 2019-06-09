package ip;

public class UnknownIPVersionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnknownIPVersionException() {
        super();
    }

    public UnknownIPVersionException(String why) {
        super(why);
    }
}
