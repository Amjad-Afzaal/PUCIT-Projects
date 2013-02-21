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
package takatuka.verifier.logic;

import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.dataObjs.constantPool.base.*;
import takatuka.classreader.logic.constants.*;
import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.logic.factory.*;
import takatuka.util.*;
import takatuka.verifier.logic.exception.*;
import takatuka.classreader.logic.util.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * Based on http://java.sun.com/docs/books/jvms/second_edition/html/ClassFile.doc.html (section 4.9.1)
 * we check here following
 * 2. Ensures that the referenced method or field exists in the given class.
 * Check following instruction. 1) invokes<> 2) getfield 3) putfield 4) get/setstatic jsr? check others...????
 * -- First take index to go to the referenceinfo.
 * -- Then go to the class and check if that class has the method/field.
 * 3. Checks that the referenced method or field has the indicated descriptor.
 *
 * 4. Checks that the currently executing method has access to the referenced method or field.
 * It is done using class AccessVerifier. See it for detail description.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 *
 */
public class Pass4 {

    private static final Pass4 pass4 = new Pass4();
    private Pass3 pass3 = Pass3.getInstanceOf();
    private Instruction currentInst = null;
    private static FactoryFacade factory = FactoryPlaceholder.getInstanceOf().
            getFactory();
    private MethodInfo currentMethod = null;

    private Pass4() {
        super();
    }

    public static Pass4 getInstanceOf() {
        return pass4;
    }

    /**
     * exceute should be called to trigger Pass4.
     * Although Pass4 usually are made to execute during runing/linking of the
     * program. However, we perform it on PC instead of mote.
     *
     */
    public void execute() {
        ClassFileController cont = ClassFileController.getInstanceOf();
        ClassFile file = null;
        MethodInfoController mCont = null;
        CodeAtt codeAtt = null;
        AccessFlags accessFlag = null;
        Oracle oracle = Oracle.getInstanceOf();
        try {
            for (int loop = 0; loop < cont.getCurrentSize(); loop++) {
                file = (ClassFile) cont.get(loop);
                ClassFile.currentClassToWorkOn = file;
                //System.out.println("class file name = " + file.getFullyQualifiedClassName());
                if (file.getFullyQualifiedClassName().contains("AddTwo")) {
                   // System.out.println("stop here");
                }
                //Miscellaneous.println("Class File "+file.getActualClassName());
                mCont = file.getMethodInfoController();
                for (int methodLoop = 0; methodLoop < mCont.getCurrentSize();
                        methodLoop++) {
                    ClassFile.currentClassToWorkOn = file;
                    currentMethod = (MethodInfo) mCont.get(methodLoop);
                    String name = oracle.methodOrFieldName(currentMethod, file.getConstantPool());
                    //System.out.println("method name ="+name);
                    accessFlag = currentMethod.getAccessFlags();
                    //Miscellaneous.println("method number "+methodNumber);
                    codeAtt = currentMethod.getCodeAtt();

                    //every method should have not null code Attribute other than native or abstract
                    if ((codeAtt != null && accessFlag.isNativeOrAbstract()) ||
                            (codeAtt == null && !accessFlag.isNativeOrAbstract())) {
                        throw new VerifyErrorExt(Messages.NON_NULL_CODEATT +
                                " at method =" + oracle.methodOrFieldName(currentMethod, file.getConstantPool()));
                    } else if (codeAtt != null) {
                        ClassFile.currentClassToWorkOn = file;
                        if (this.currentMethodNameAndDesc().equals("java/lang/Thread-->run---()V")) {
                           // Miscellaneous.println("Stop here");
                        }
                        checkReferenceMethods(currentMethod.getCodeAtt());
                    }
                }
            }
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    /**
     * Check following two things
     * 1. If the method/field exist
     * 2. If the current class can access that field. For it used AccessVerifier class
     * @param cAtt CodeAtt
     * @throws Exception
     */
    private void checkReferenceMethods(CodeAtt cAtt) {

        
    }

    //todo might not be a good place to add this function...
    private String getRefKey(MultiplePoolsFacade pOne, ReferenceInfo refInfo) throws Exception {
        NameAndTypeInfo nameAndType = (NameAndTypeInfo) getCP(pOne, refInfo.getNameAndTypeIndex(),
                CPTagValues.CONSTANT_NameAndType);
        Un desc = nameAndType.getDescriptorIndex();
        Un name = nameAndType.getIndex();
        return desc + ", " + name;
    }

    private String currentMethodNameAndDesc() {
        ClassFile currentClassFile = ClassFile.currentClassToWorkOn;
        MultiplePoolsFacade pOne = currentClassFile.getConstantPool();
        String name = Oracle.getInstanceOf().methodOrFieldName(currentMethod, pOne);
        String desc = Oracle.getInstanceOf().methodOrFieldDescription(currentMethod, pOne);
        return currentClassFile.getFullyQualifiedClassName() + "-->" + name + "---" + desc;
    }

    private void checkReferenceMethods(MultiplePoolsFacade pOne, int cpIndex, int tag) throws VerifyErrorExt, Exception {
        InfoBase base = getCP(pOne, cpIndex, tag);
        
        AccessVerifier accVerifier = AccessVerifier.getInstanceOf();
        if (!(base instanceof ReferenceInfo)) {
            throw new VerifyErrorExt(Messages.REFERENCE_NOT_EXIST,
                    currentMethodNameAndDesc(), currentInst);

        }
        ReferenceInfo refInfo = (ReferenceInfo) base;
        ClassFile classFile = Oracle.getInstanceOf().getClass(refInfo.getIndex(),
                pOne);
        if (classFile == null) {
            throw new VerifyErrorExt("Cannot find class file referred # " +
                    refInfo.getIndex() + ", in class " +
                    ClassFile.currentClassToWorkOn.getFullyQualifiedClassName() +
                    ". Probability a loading error.",
                    currentMethodNameAndDesc(),
                    currentInst);
        }
        FieldInfo field = null;
        // Miscellaneous.println(classFile.getFullyQualifiedClassName());
        boolean isMethod = tag != CPTagValues.CONSTANT_Fieldref;
        field = classFile.hasMethodOrField(getRefKey(pOne, refInfo), !isMethod);

        // a method could be in a super class
        while (field == null && classFile.getSuperClass() != null &&
                classFile.getSuperClass().intValueUnsigned() != 0) {
            classFile = Oracle.getInstanceOf().getClass(classFile.getSuperClass(),
                    classFile.getConstantPool());
            Miscellaneous.println("Super class = " + classFile.getFullyQualifiedClassName());
            field = classFile.hasMethodOrField(getRefKey(pOne, refInfo), !isMethod);
            Miscellaneous.println("Super class pointer = " +
                    classFile.getSuperClass());
        }
        if (field == null) {
            String msg = Messages.METHOD_NOT_EXIST;
            if (!isMethod) {
                msg = Messages.FIELD_NOT_EXIST;
            }
            throw new VerifyErrorExt(msg, currentMethodNameAndDesc(), currentInst);
        }
        //now we know that field exist. We now check its access
        accVerifier.execute(ClassFile.currentClassToWorkOn, classFile,
                field);
    }

    private InfoBase getCP(MultiplePoolsFacade pOne, int index, int tag) {
        try {
            
            return (InfoBase) pOne.get(index, tag);
        } catch (Exception d) {
            Miscellaneous.println(this.currentMethodNameAndDesc());
            throw new VerifyError(Messages.CP_INDEX_INVALID+", index="+index+", CP-tag="+tag);
        }
    }

    private InfoBase getCP(MultiplePoolsFacade pOne, Un index, int tag) {
        return getCP(pOne, index.intValueUnsigned(), tag);
    }
}
