package takatuka.verifier.dataObjs;

import takatuka.classreader.dataObjs.MethodInfo;
import takatuka.util.Oracle;

/**
 * <p>Title: </p>
 * <p>Description:
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class FrameMaxInfo {

    public int maxStackSize = 0;
    public int maxLocalsSize = 0;
    public String methodStr = null;

    public FrameMaxInfo(MethodInfo method) {
        this.methodStr = Oracle.getInstanceOf().getMethodOrFieldString(method);
    }
    public FrameMaxInfo(MethodInfo method, int maxStackSize, int maxLocalsSize) {
        this(method);
        this.maxStackSize = maxStackSize;
        this.maxLocalsSize = maxLocalsSize;
    }

    @Override
    public String toString() {
        String ret = "";
        ret +="Method="+methodStr+", Max-Stack="+maxStackSize+", Max-LVs="+maxLocalsSize+"\n";
        return ret;
    }
}
