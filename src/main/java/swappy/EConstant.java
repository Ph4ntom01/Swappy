package swappy;

public enum EConstant {

    SWAPPY_VERSION("1.1"), FRAME_BORDER_DARK("#F0E68C"), FRAME_BORDER_LIGHT("#6290F4"), BORDER_THICKNESS("3");

    private String constant;

    private EConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }

}
