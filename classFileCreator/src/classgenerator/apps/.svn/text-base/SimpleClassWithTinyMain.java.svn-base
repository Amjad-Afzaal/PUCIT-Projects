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

import classgenerator.logic.*;
import deadCodeRmv.StartMeDCR;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.logic.constants.*;
import takatuka.classreader.logic.factory.*;

/**
 * For automatically generating.
 *
 * @author Faisal Aslam
 */
public class SimpleClassWithTinyMain extends StartMeDCR {

    public SimpleClassWithTinyMain() {
    }

    @Override
    public void execute(String args[]) throws Exception {

        try {
            Oracle oracle = new OracleImpl();
            FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
            ClassFile cFile = oracle.createEmptyClassFile("pack1/SimpleClass", "SimpleClass.java");
            /**
             * added the main method in the class file
             */
            MethodInfo mainMethod = oracle.addMainFunction(cFile);
            oracle.addDefaultConstructor(cFile);
            /**
             * Now going to add code in the method.
             * BIPUSH 0x09
            0x2:5   ISTORE_1 
            0x3:6   RETURN
             */
            Un UnByte = factoryFacade.createUn(9).trim(1);
            Instruction BIPUST = factoryFacade.createInstruction(JavaInstructionsOpcodes.BIPUSH,
                    UnByte, mainMethod.getCodeAtt());
            oracle.addInstruction(cFile, mainMethod, BIPUST);

            Instruction iStore1 = factoryFacade.createInstruction(JavaInstructionsOpcodes.ISTORE_1, mainMethod.getCodeAtt());
            oracle.addInstruction(cFile, mainMethod, iStore1);

            Instruction returnInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.RETURN, mainMethod.getCodeAtt());
            oracle.addInstruction(cFile, mainMethod, returnInstr);

            /**
             * It will automatically set the frame size values
             * and do the dead code removal.
             */
            super.execute(args);


        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String args[]) throws Exception {
        (new SimpleClassWithTinyMain()).start(args);
    }
}
