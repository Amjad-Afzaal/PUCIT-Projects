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
import takatuka.classreader.dataObjs.attribute.Instruction;
import takatuka.classreader.logic.*;
import takatuka.classreader.logic.constants.JavaInstructionsOpcodes;
import takatuka.classreader.logic.factory.*;

/**
 *
 * This is the basic helloWorld class
 * generated without using any source.
 * 
 * @author Faisal Aslam
 */
public class HelloWorldGenerator extends StartMeDCR {

    public static void main(String args[]) throws Exception {
        new HelloWorldGenerator().start(args);
    }

    @Override
    public void execute(String args[]) throws Exception {
        Oracle oracle = new OracleImpl();
        FactoryFacade factoryFacade = FactoryPlaceholder.getInstanceOf().getFactory();
        ClassFile cFile = oracle.createEmptyClassFile("HelloWorld", "HelloWorld.java");
        oracle.addDefaultConstructor(cFile);
        /**
         * added the main method in the class file without any code
         */
        MethodInfo mainMethod = oracle.addMainFunction(cFile);
        oracle.addInstruction(cFile, mainMethod, 
                factoryFacade.createInstruction(JavaInstructionsOpcodes.ICONST_5, mainMethod.getCodeAtt()));
        oracle.addInstruction(cFile, mainMethod, 
                factoryFacade.createInstruction(JavaInstructionsOpcodes.ISTORE_1, mainMethod.getCodeAtt()));
        
        oracle.print(cFile, mainMethod, "Hello World!");
        //oracle.print(cFile, mainMethod, 1, true);
        
        /**
         * return 
         */
        Instruction retInstr = factoryFacade.createInstruction(JavaInstructionsOpcodes.RETURN, mainMethod.getCodeAtt());
        oracle.addInstruction(cFile, mainMethod, retInstr);

        /**
         * Do the dead code removal and frame size automatic calculation.
         */
        super.execute(args);
    }
}
