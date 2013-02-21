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
package deadCodeRmv.logic;

import deadCodeRmv.UsefulFun;
import deadCodeRmv.dataObjs.*;
import java.util.*;

/**
 *
 *
 * @author Faisal Aslam
 */
public class RemoveClassesMethodsAndBytecode {

    /**
     * It removes classes, method and bytecode.
     * You do not need to change this method as it is complete.
     */
    public void remove() {
        removeClasses();
    }

    /**
     * Go through all the classes and removeClasses any class that 
     * has shouldkeep = false.
     * For each class kept go through all the functions and
     * keep functions that has shouldKeep = true.
     * 
     * Note: This function should call removeMethods function
     * for only the class file that is kept. 
     */
    private void removeClasses() {
        Collection<ClassFileDCR> allClassFiles = UsefulFun.getAllClassFiles();
        Iterator<ClassFileDCR> iterate = allClassFiles.iterator();
        while(iterate.hasNext())
        {
            ClassFileDCR c = iterate.next();
            if(c.getShouldKeep())
                removeMethods(c);
            else
            {   UsefulFun.removeClass(c); System.out.println(" Remove class = " + c.getFullyQualifiedClassName()); }
        }
    }

    /**
     * go through all the methods of a given class file and removeClasses
     * each method that has shouldkeep flag unset
     * This method is called from removeClasses
     * 
     * Note: This method calls removeBytecode for each method that it keeps.
     * 
     * @param cFile 
     */
    private void removeMethods(ClassFileDCR cFile) {
        Collection<MethodInfoDCR> allMethods = UsefulFun.getallMethods(cFile);
        Iterator<MethodInfoDCR> iterate = allMethods.iterator();
        while(iterate.hasNext())
        {
            MethodInfoDCR method = iterate.next();
            if(method.getShouldKeep())
                removeBytecode(method);
            else
            {   UsefulFun.removeMethod(cFile, method); System.out.println(" Remove method = " + method.getKey());}
        }
    }

    /**
     * go through the bytecode 
     * removeBytecode the instructions that are not analyzed and thus have the 
     * shouldKeep flag false.
     */
    private void removeBytecode(MethodInfoDCR method) {
        Collection<InstructionDCR> allInstructions = UsefulFun.getAllInstructions(method);
        Iterator<InstructionDCR> iterate = allInstructions.iterator();
        while(iterate.hasNext())
        {
            InstructionDCR instruction = iterate.next();
            if(instruction.getShouldKeep() == false)
            {   UsefulFun.removeInstruction(instruction); System.out.println(" Remove instruction = " + instruction.getMnemonic());}
        }
    }
}