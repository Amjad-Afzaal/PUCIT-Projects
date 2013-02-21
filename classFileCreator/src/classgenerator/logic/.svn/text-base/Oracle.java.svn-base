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

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.CodeAtt;

/**
 *
 * 
 * @author ´Faisal Aslam
 */
public interface Oracle extends OracleHighLevel {
         
    public AccessFlags generateAccessFlag(ArrayList<Integer> accessFlagValues);

    /**
     * Given a string it inserts a UTF8Info in the constant pool (CP)
     * and return the newly added index. In case that string already 
     * exist then the function return index already present CP value.
     * 
     * @param cFile class file to be insert UTF8 in
     * @param str string to be inserted in the CP
     * @return
     * @throws Exception 
     */
    public int addUTF8InCP(ClassFile cFile, String str) throws Exception;

    /**
     * Insert classInfo in the constant pool of a given class file.
     * 
     * @param cFile
     * @param nameUTF8Index
     * @return
     * @throws Exception 
     */
    public int addClassInfoInCP(ClassFile cFile, int nameUTF8Index) throws Exception;

    /**
     * Adds source attribute in a class file.
     * @param cFile
     * @param sourceFileName
     * @throws Exception 
     */
    public void addSourceFileAttribute(ClassFile cFile, String sourceFileName) throws Exception;

    /**
     * returns the name of the super class of the given class file.
     * @param cFile
     * @return 
     */
    public String getSuperClassName(ClassFile cFile);

    public int findClassInfoFromCP(ClassFile cFile, String className);

    public int findUTF8FromCP(ClassFile cFile, String utf8);

    public int addMethodRefInCP(ClassFile cFile, int cInfoCPIndex,
            String methodName, String methodDesc);

    public int addFieldRefInCP(ClassFile cFile, int cInfoCPIndex,
            String methodName, String methodDesc);

    public int addNameAndTypeInfo(ClassFile cFile, String name, String Desc);
    
    public int addStringInfoInCP(ClassFile cFile, String name);
    
    public MethodInfo addDefaultConstructor(ClassFile cFile) throws Exception;
    
    public String getUTF8FromCP(ClassFile cFile, int cpIndex);
}
