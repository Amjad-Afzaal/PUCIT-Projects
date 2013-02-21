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
 * Please contact Faisal Aslam Copyright 2012
 * if you need additional information or have any questions.
 */
package classgenerator.apps;

import java.util.ArrayList;
import classgenerator.logic.*;
import deadCodeRmv.StartMeDCR;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.constants.FieldTypes;
import takatuka.classreader.logic.constants.JavaInstructionsOpcodes;
import takatuka.classreader.logic.factory.*;

/**
 *
 * This is the basic helloWorld class
 * generated without using any source.
 * 
 * @author Faisal Aslam
 */
public class AddTwoGen extends StartMeDCR {

    private static final String FUN_NAME = "addTwo";

    public static void main(String args[]) throws Exception {
        new AddTwoGen().start(args);
    }

    private MethodInfo toAddAddTwoFun(ClassFile cFile) {
        MethodInfo newMethod = null;
        try {
            Oracle oracle = new OracleImpl();
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            ArrayList<FieldType> parm = new ArrayList<FieldType>();
            parm.add(new FieldType(FieldTypes.INTEGER));
            parm.add(new FieldType(FieldTypes.INTEGER));
            FieldType ret = new FieldType(FieldTypes.INTEGER);
            newMethod = oracle.addFunction(cFile, FUN_NAME, parm, ret);
            oracle.addInstruction(cFile, newMethod,
                    factoryFacade.createInstruction(JavaInstructionsOpcodes.ILOAD_1, newMethod.getCodeAtt()));
            oracle.addInstruction(cFile, newMethod, factoryFacade.createInstruction(JavaInstructionsOpcodes.ILOAD_2, newMethod.getCodeAtt()));
            oracle.addInstruction(cFile, newMethod,
                    factoryFacade.createInstruction(JavaInstructionsOpcodes.IADD, newMethod.getCodeAtt()));
            oracle.addInstruction(cFile, newMethod,
                    factoryFacade.createInstruction(JavaInstructionsOpcodes.IRETURN, newMethod.getCodeAtt()));

        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return newMethod;
    }

    @Override
    public void execute(String args[]) throws Exception {

        Oracle oracle = new OracleImpl();
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        ClassFile cFile = oracle.createEmptyClassFile("AddTwo11", "AddTwo11.java");



        MethodInfo constrMethodInfo = oracle.addDefaultConstructor(cFile);

        int defaultConstrRefInfo = oracle.addMethodRefInCP(cFile, cFile.getThisClass().intValueSigned(),
                oracle.getUTF8FromCP(cFile, constrMethodInfo.getNameIndex().intValueSigned()),
                oracle.getUTF8FromCP(cFile, constrMethodInfo.getDescriptorIndex().intValueSigned()));

        MethodInfo addTwo = toAddAddTwoFun(cFile);

        /**
         * added the main method in the class file without any code
         */
        MethodInfo mainMethod = oracle.addMainFunction(cFile);
        oracle.createObj(cFile, mainMethod, cFile.getThisClass().intValueUnsigned(),
                defaultConstrRefInfo);

        /**
         * ICOST_1
         * ICOST_5
         */
        oracle.addInstruction(cFile, mainMethod, factoryFacade.createInstruction(JavaInstructionsOpcodes.ICONST_1, mainMethod.getCodeAtt()));
        oracle.addInstruction(cFile, mainMethod,
                factoryFacade.createInstruction(JavaInstructionsOpcodes.ICONST_5, mainMethod.getCodeAtt()));
        /**
         * Going to invoke addTwo function
         */
        int methodCPIndex = oracle.addMethodRefInCP(cFile, cFile.getThisClass().intValueSigned(),
                oracle.getUTF8FromCP(cFile, addTwo.getNameIndex().intValueSigned()),
                oracle.getUTF8FromCP(cFile, addTwo.getDescriptorIndex().intValueSigned()));
        oracle.addInstruction(cFile, mainMethod, factoryFacade.createInstruction(
                JavaInstructionsOpcodes.INVOKEVIRTUAL,
                factoryFacade.createUn(methodCPIndex).trim(2), mainMethod.getCodeAtt()));

        /**
         * ISTORE_1
         */
        oracle.addInstruction(cFile, mainMethod,
                factoryFacade.createInstruction(JavaInstructionsOpcodes.ISTORE_1, mainMethod.getCodeAtt()));

        oracle.print(cFile, mainMethod, 1, true);
        /**
         * return 
         */
        oracle.addInstruction(cFile, mainMethod,
                factoryFacade.createInstruction(JavaInstructionsOpcodes.RETURN, mainMethod.getCodeAtt()));

        super.execute(args);
    }
}
