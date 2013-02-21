/*
 * Copyright 2012 Christian Schindelhauer, Peter Thiemann, Faisal Aslam, Luminous Fennell and Gidon Ernst.
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
 * Please contact Faisal Aslam 
 * (faisal.aslam AT gmail.com)
 * if you need additional information or have any questions.
 */
package takatuka.util;

import takatuka.classreader.dataObjs.*;
import java.util.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.logic.*;

/**
 * 
 * Description:
 * <p>
 *
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
class FieldMethodOracle {

    private static final String INSTANCE_INITIALIZATION_METHOD_NAME = "<init>";
    private static final String CLASS_INTERFACE_INIT_METHOD_NAME = "<clinit>";
    private static final String EMPTY_DESC = "()V";
    private static final FieldMethodOracle fmOracle = new FieldMethodOracle();
    //private static final GlobalConstantPool cp = GlobalConstantPool.getInstanceOf();
    private Vector<CodeAttCache> allCodeAttributeCache = null;
    private Vector<ExceptionAttCache> allExceptionAttributeCache = null;
    private Vector allMethodsCache = null;

    private FieldMethodOracle() {
    }

    static FieldMethodOracle getInstanceOf() {
        return fmOracle;
    }

    final void clearCache() {
        allCodeAttributeCache = null;
        allExceptionAttributeCache = null;
        allMethodsCache = null;
    }

    final int getTotalStackSizeOfAllMethods() {
        Vector<CodeAttCache> codeAttCache = getAllCodeAtt();
        Iterator<CodeAttCache> it = codeAttCache.iterator();
        int ret = 0;
        while (it.hasNext()) {
            CodeAttCache cAtt = it.next();
            MethodInfo method = cAtt.getMethodInfo();
            ret += method.getCodeAtt().getMaxStack().intValueUnsigned();
        }
        return ret;
    }

    final int getTotalLVSizeOfAllMethods() {
        Vector<CodeAttCache> codeAttCache = getAllCodeAtt();
        Iterator<CodeAttCache> it = codeAttCache.iterator();
        int ret = 0;
        while (it.hasNext()) {
            CodeAttCache cAtt = it.next();
            MethodInfo method = cAtt.getMethodInfo();
            ret += method.getCodeAtt().getMaxLocals().intValueUnsigned();
        }
        return ret;
    }

    final boolean isEmptyContructor(MultiplePoolsFacade pOne, 
            MethodInfo method) {
        
        String methodName = Oracle.getInstanceOf().methodOrFieldName(method, pOne);
        String methodDesc = Oracle.getInstanceOf().methodOrFieldDescription(method, pOne);
        if (methodName.equals(INSTANCE_INITIALIZATION_METHOD_NAME)
                && methodDesc.equals(EMPTY_DESC)) {
            return true;
        }
        return false;
    }

    final boolean isInstanceInitMethod(String methodName) {
        if (methodName.equals(INSTANCE_INITIALIZATION_METHOD_NAME)) {
            return true;
        }
        return false;
    }

    final boolean isClassInterfaceInitMethod(MultiplePoolsFacade pOne, MethodInfo method) {
        String methodName = Oracle.getInstanceOf().
                methodOrFieldName(method, pOne);
        return isClassInterfaceInitMethod(methodName);
    }

    final boolean isClassInterfaceInitMethod(String methodName) {
        if (methodName.equals(CLASS_INTERFACE_INIT_METHOD_NAME)) {
            return true;
        }
        return false;
    }

    final Vector<MethodInfo> getAllMethodInfo() {
        if (allMethodsCache != null) {
            //return (Vector<MethodInfoCache>) allMethodsCache.clone();
        }
        ClassFileController cont = ClassFileController.getInstanceOf();
        int size = cont.getCurrentSize();
        int mContSize = 0;
        Vector<MethodInfo> ret = new Vector();
        for (int cfIndex = 0; cfIndex < size; cfIndex++) {
            ClassFile cFile = (ClassFile) cont.get(cfIndex);
            MethodInfoController mCont = cFile.getMethodInfoController();
            mContSize = mCont.getCurrentSize();
            for (int mIndex = 0; mIndex < mContSize; mIndex++) {
                MethodInfo method = (MethodInfo) mCont.get(mIndex);
                ret.addElement(method);
            }
        }
        if (StartMeAbstract.getCurrentState() == StartMeAbstract.STATE_READ_FILES) {
            allMethodsCache = ret;
        }
        return (Vector<MethodInfo>) ret.clone();
    }

    final Vector<ExceptionAttCache> getAllExceptionAtt() {
        if (allExceptionAttributeCache != null) {
            return (Vector<ExceptionAttCache>) allExceptionAttributeCache.clone();
        }
        return getAllAttribute(false);

    }

    final Vector<CodeAttCache> getAllCodeAtt() {
        if (allCodeAttributeCache != null) {
            return (Vector<CodeAttCache>) allCodeAttributeCache.clone();
        }
        return getAllAttribute(true);
    }

    private Vector getAllAttribute(boolean isCodeAtt) {
        ClassFileController cont = ClassFileController.getInstanceOf();
        int size = cont.getCurrentSize();
        int mContSize = 0;
        Vector ret = new Vector();
        AttributeInfo attInfo = null;
        CodeAttCache cainfo = null;
        for (int cfIndex = 0; cfIndex < size; cfIndex++) {
            ClassFile cFile = (ClassFile) cont.get(cfIndex);
            MethodInfoController mCont = cFile.getMethodInfoController();
            mContSize = mCont.getCurrentSize();
            for (int mIndex = 0; mIndex < mContSize; mIndex++) {
                MethodInfo method = (MethodInfo) mCont.get(mIndex);
                if (isCodeAtt) {
                    attInfo = method.getCodeAtt();
                    cainfo = new CodeAttCache(attInfo, method, cFile);
                } else {
                    attInfo = method.getExceptionAtt();
                    cainfo = new ExceptionAttCache(attInfo, method, cFile);
                }
                if (attInfo != null) {
                    ret.addElement(cainfo);
                }
            }
        }
        if (StartMeAbstract.getCurrentState() == StartMeAbstract.STATE_READ_FILES) {
            if (isCodeAtt) {
                allCodeAttributeCache = ret;
            } else {
                allExceptionAttributeCache = ret;
            }
        }
        return (Vector) ret.clone();
    }

    final void updateAllCodeAttsLength() {
        Vector<CodeAttCache> codeAttVec = getAllCodeAtt();
        CodeAtt codeAtt = null;

        for (int loop = 0; loop < codeAttVec.size(); loop++) {
            codeAtt = (CodeAtt) codeAttVec.elementAt(loop).getAttribute();
            if (codeAtt == null || codeAtt.getCodeLength().intValueUnsigned() == 0) {
                continue;
            }
            codeAtt.updateCodeLength();
        }
    }

    final long getTotalCodeLength() {
        Vector<CodeAttCache> codeAttVec = getAllCodeAtt();
        CodeAtt codeAtt = null;
        long totalLength = 0;
        for (int loop = 0; loop < codeAttVec.size(); loop++) {
            codeAtt = (CodeAtt) codeAttVec.elementAt(loop).getAttribute();
            if (codeAtt == null || codeAtt.getCodeLength() == null) {
                continue;
            }
            codeAtt.updateCodeLength();
            totalLength += (long) codeAtt.getCodeLength().intValueUnsigned();
        }
        return totalLength;
    }

    final HashSet<OpcodeMnemonicInUse> getAllOpcodeMnemonicsUseInCode() {
        Vector<CodeAttCache> codeAttVec = Oracle.getInstanceOf().getAllCodeAtt();
        CodeAtt codeAtt = null;
        HashSet<OpcodeMnemonicInUse> ret = new HashSet();
        for (int loop = 0; loop < codeAttVec.size(); loop++) {
            codeAtt = (CodeAtt) codeAttVec.elementAt(loop).getAttribute();
            if (codeAtt == null || codeAtt.getCodeLength() == null) {
                continue;
            }
            Vector<Instruction> instVec = codeAtt.getInstructions();
            for (int instIndex = 0; instIndex < instVec.size(); instIndex++) {
                Instruction inst = instVec.elementAt(instIndex);
                ret.add(new OpcodeMnemonicInUse(inst.getMnemonic(), inst.getOpCode()));
            }
        }
        return ret;
    }
}