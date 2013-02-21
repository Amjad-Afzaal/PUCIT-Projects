/*
 * Copyright 2012 Faisal Aslam.
 * All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER
 * 
 * This code is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3
 * only, as published by the Free Software Foundation.
 * 
 * This code is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License version 3 for more details (a copy is
 * included in the LICENSE file that accompanied this code).
 * 
 * You should have received a copy of the GNU General Public License
 * version 3 along with this work; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 * 
 * Please contact Faisal Aslam (faisal.aslam AT gmail.com)
 * if you need additional information or have any questions.
 */
package deadCodeRmv.dataObjs;

import deadCodeRmv.UsefulFun;
import takatuka.classreader.dataObjs.ClassFile;
import takatuka.verifier.dataObjs.VerifyMethodInfo;

/**
 *
 *
 * @author Faisal Aslam
 */
public class MethodInfoDCR extends VerifyMethodInfo implements ShouldKeepInterface {

    /**
     * true if the method should be kept.
     * By default every method should be up for deletion.
     */
    private boolean shouldKeep = false;
    
    /**
     * should be true if a method is already analyzed
     */
    private boolean isAlreadyAnalyzed = false;

    public MethodInfoDCR(ClassFile myClassFile) {
        super(myClassFile);
    }

    /**
     * set true if the method should be kept otherwise
     * set it false
     * @param shouldKeep 
     */
    public void setShouldKeep(boolean shouldKeep) {
        this.shouldKeep = shouldKeep;
    }

    /**
     * 
     * @return 
     */
    public boolean getShouldKeep() {
        String name = UsefulFun.getMethodName(this);
        if (name != null && name.trim().startsWith("<")) {
            return true; //do not remove constructors and clinit methods.
        }
        return shouldKeep;
    }
    
    /**
     * Should be set after a function is analyzed
     */
    public void setAlreadyAnalyzed() {
        this.isAlreadyAnalyzed = true;
    }
    
    /**
     * Should be check before starting analyzing a function.
     * To make sure that a same function is not analyzed twice or more. 
     * @return 
     */
    public boolean isAlreadyAnalyzed() {
        return isAlreadyAnalyzed;
    }
}
