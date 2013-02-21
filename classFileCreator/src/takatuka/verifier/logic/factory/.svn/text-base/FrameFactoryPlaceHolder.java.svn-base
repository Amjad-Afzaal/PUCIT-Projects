package takatuka.verifier.logic.factory;

/**
 * <p>Title: </p>
 * <p>Description:
 * Based on the placeholder design pattern.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class FrameFactoryPlaceHolder {
    private VerificationFrameFactory factory = VerificationFrameFactory.getInstanceOf();
    private static final FrameFactoryPlaceHolder placeholder = new
            FrameFactoryPlaceHolder();

    private FrameFactoryPlaceHolder() {
        super();
    }

    public static FrameFactoryPlaceHolder getInstanceOf() {
        return placeholder;
    }

    public void setFactory(VerificationFrameFactory factory) {
        this.factory = factory;
    }

    public VerificationFrameFactory getFactory() {
        return factory;
    }

}
