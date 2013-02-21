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
package classgenerator.logic;

import takatuka.classreader.dataObjs.*;
import java.util.*;
import takatuka.classreader.dataObjs.attribute.Instruction;

/**
 *
 * @author Faisal Aslam
 */
public interface OracleHighLevel {

    /**
     * It adds This or super pointer in a class file.
     * 
     * @param cFile
     * @param name
     * @param isThisPointer if true then it is a this pointer otherwise a super pointer.
     * @throws Exception 
     */
    public void addThisOrSuperPointer(ClassFile cFile, String name, boolean isThisPointer) throws Exception;

    /**
     * Add a new function or a constructor with public access flag. 
     * @param cFile
     * @param functionName
     * @param callingParmeterTypes is one of the byte defined in the FieldTypes class
     * @param returnType is one of the byte defined in the FieldTypes class
     */
    public MethodInfo addFunction(ClassFile cFile, String functionName, ArrayList<FieldType> callingParmeterTypes,
            FieldType returnType);

    /**
     * Add a new function give a classname and the function name
     * @param cFile
     * @param functionName 
     */
    public MethodInfo addFunction(ClassFile cFile, String functionName);

    /**
     * Add a new function or a constructor with the given access flag. 
     * @param AccessFlagValue get the value from AccessFlagValues class
     * @param cFile
     * @param functionName
     * @param callingParmeterTypes is one of the byte defined in the FieldTypes class
     * @param returnType is one of the byte defined in the FieldTypes class
     */
    public MethodInfo addFunction(int AccessFlagValue, ClassFile cFile, String functionName, ArrayList<FieldType> callingParmeterTypes,
            FieldType returnType);

    /**
     * add a new instruction in the given method of the given class file.
     * However, please set max-stack and max-local yourself with the given function
     * (rest of the things are taken care off).
     * 
     * @param cFile
     * @param method
     * @param instr 
     */
    public void addInstruction(ClassFile cFile, MethodInfo method, Instruction instr);

    /**
     * The class file has the default constructor but no other function.
     * 
     * @param className
     * @param javaSourceName
     * @return
     * @throws Exception 
     */
    public ClassFile createEmptyClassFile(String className, String javaSourceName) throws Exception;

    /**
     * Give a class file it adds a main method.
     * 
     * @param cFile
     * @return the newly created/added method
     */
    public MethodInfo addMainFunction(ClassFile cFile);

    public int addInCP(ClassFile cFile, Object object) throws Exception;
    
    /**
     * add System.out.print for the given string in the class file.
     * 
     * @param cFile
     * @param method
     * @param toPrint 
     */
    public void print(ClassFile cFile, MethodInfo method, String toPrint);

    /**
     * add System.out.print for the given local variable in the class file.
     * Note that the local variable supported are int and float.
     * 
     * @param cFile
     * @param method
     * @param LVIndex this is the index of the local variable array
     * @param isInt if true then local variable is integer. otherwise it is a float.
     */
    public void print(ClassFile cFile, MethodInfo method, int LVIndex, boolean isInt);

    /**
     * 
    /**
     * create an object of a given type and then calls it default constructor
     * @param cFile
     * @param method
     * @param cpClassInfoIndex
     * @param defaultConstrCPIndex 
     */
    public void createObj(ClassFile cFile, MethodInfo method, int cpClassInfoIndex,
            int defaultConstrCPIndex);

}
