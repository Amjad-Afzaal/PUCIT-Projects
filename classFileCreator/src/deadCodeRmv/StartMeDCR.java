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
package deadCodeRmv;

import deadCodeRmv.dataObjs.*;
import deadCodeRmv.logic.RemoveClassesMethodsAndBytecode;
import deadCodeRmv.logic.factory.*;
import frameSizeCalc.logic.*;
import java.util.*;
import takatuka.classreader.dataObjs.ClassFile;
import takatuka.classreader.logic.factory.*;
import takatuka.optimizer.bytecode.branchSetter.logic.BranchInstructionsHandler;
import takatuka.verifier.logic.factory.*;

/**
 * This file performs basic deadcode removal.
 * 
 * Input: the deadcode code takes classfile with main method name
 * as the input parameters.
 * 
 * Output: the set of method (with smaller code) and classfiles to be
 * made part of program.
 * 
 * The deadcode removal has two phases. 
 * 
 * Phase 1: analyzes the code inside a method.
 * Phase 2: tries to find out what methods and classes to analyze and removeClasses.
 * 
 * *********** Phase 1 ****
 * 
 * *- code removal
 * Use the data-flow analyzer to keep track of types and their values
 * whenever possible. In case part of code is not visited because of some
 * condition then removeClasses that code.
 * 
 * *- local variable removal
 * In case a local variable that is not input parameter of the function
 * is never get then removeClasses that parameter even if that parameter is set
 * in the function.
 * 
 * ************ Phase 2 *****
 * 
 **- method removal
 * - Only analyze (and do not removeClasses) the method whose names are found while 
 *    analyzing any of the previous methods.
 * 
 * *- classfiles removal
 * - Continue making a list of classfiles for which a "new" instruction is found.
 * --- At the end of phase 2 removeClasses all classfiles that are NOT in the above list and
 * also are not super-class of any of the classfile in the above list.
 * 
 *
 * @author Faisal Aslam
 */
public class StartMeDCR extends StartMeFSC {

    public static String MAIN_CLASS_NAME = "jvmTestCases/Main";
    private static ArrayList<MethodInfoDCR> toBeAnalyzed = new ArrayList<MethodInfoDCR>();
    
    @Override
    public void setFactoryFacade() {
        super.setFactoryFacade();
        FactoryPlaceholder.getInstanceOf().setFactory(FactoryFacadeDCR.getInstanceOf());
    }

    @Override
    public void validateInput(String args[]) throws Exception {
        if (args.length < 2 || args.length > 3) {
            System.out.println(
                    "StartMeDCR <input file-name/directory-Name> "
                    + "<output directory-Name> <Optional: Class name of with main function>");
            System.out.println(
                    "Example: StartMeDCR input/ output/ jvmTestCases/Main");

            System.out.println(
                    " If the input file is a directory then it recursively traverse it");
            System.out.println(
                    " The program will stop on the first file, not conforms with class-file-format");
            System.out.println(
                    " The output directory will have classfile.class and classfile.txt files. ");
            System.out.println(
                    " The txt file contains seperated section of a classfile.");
            System.exit(1);

        }
        if (args.length > 2) {
            MAIN_CLASS_NAME = args[2];
        }
    }

    @Override
    public void execute(String args[]) throws Exception {
        /**
         * Call suer first so that classes can be loaded.
         */
        super.execute(args);
        FrameFactoryPlaceHolder.getInstanceOf().setFactory(FrameFactoryDCR.getInstanceOf());


        // if (true) return;
        /**
         * Class with main method should be kept even if
         * no object is made of it.
         */
        
        ClassFileDCR cFile = UsefulFun.getClassByName(MAIN_CLASS_NAME);
        cFile.setShouldKeep(true);
        if (cFile == null) {
            System.out.println("WARNING: Main class cannot be found");
            return; //No main is found hence we are done here.
        }
        //if (true) return;
        /**
         * now start analyzing methods.
         */
        MethodInfoDCR mainFunction = UsefulFun.getMainFunction(MAIN_CLASS_NAME);
        
        
        /**
         * Add you code here STUDENT:
         * 
         */
        
        //Add main method to toBeAnalyzed
        toBeAnalyzed.add(mainFunction);
        
        // Create an Iterator to iterate toBeAnalyzed method collection
        System.out.println("================== Enter In My Piece of Code ==================");
        for(int i=0; i<toBeAnalyzed.size(); i++)
        {
            MethodInfoDCR method = toBeAnalyzed.get(i);
            if(UsefulFun.getMethodName(method).equals(UsefulFun.getMethodName(mainFunction)) || ((ClassFileDCR) method.getClassFile()).getShouldKeep())
            {
                if(method.isAlreadyAnalyzed())  continue;
                else
                {
                    UsefulFun.DFA(method);
                    method.setAlreadyAnalyzed();
                    method.setShouldKeep(true);
                }
            }
        }
        
        if (mainFunction != null) {
            /**
             * Remove classes, functions and bytecode
             */
            System.out.println("\n\n\n============== Removing Method is Starting Now ==============\n\n\n");
            new RemoveClassesMethodsAndBytecode().remove();
            System.out.println("\n\n============== Removing Method is Ends Now ==============\n\n\n");
        } else {
            System.out.println("WARNING: Main function cannot be found");
        }
        /**
         * We might have remove some instruction. Hence we need to recalculate 
         * branch address. That is done using the following function.
         */
        BranchInstructionsHandler.getInstanceOf().restoreBranchInformation();
        super.execute(args);
    }

    public static void addFunctionTotoBeAnalyzed(MethodInfoDCR method)
    {
        toBeAnalyzed.add(method);
    }
    public static void main(String args[]) throws Exception {
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2] + "\n\n\n");
        (new StartMeDCR()).start(args);
    }

    
}
