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

import java.util.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.attribute.Instruction;
import takatuka.classreader.dataObjs.constantPool.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 * Oracle knows about everything. It has utility function that can make the file easy
 * Note that there are many Oracles. However, this Oracle is facade to all other Oracles.
 * Other Oracles have special information about their own subject 
 * (e.g. ClassFileOracle have information about ClassFile only).
 * @author Faisal Aslam
 * @version 1.0
 */
public class Oracle {

    private static final Oracle oracle = new Oracle();
    private final FieldMethodOracle fmOracle = FieldMethodOracle.getInstanceOf();
    private final ClassFileOracle cfOracle = ClassFileOracle.getInstanceOf();

    // private final GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
    private Oracle() {
        super();
    }

    public static Oracle getInstanceOf() {
        return oracle;
    }

    /**
     * Given an instruction it return CP-Index used in that instruction 
     * (i.e. from the instruction operand).
     * In case the given instruction does not use any CP-Index then the function
     * returns -1;
     * 
     * @param inst
     * @return 
     */
    public final int getCPIndex(Instruction inst) {
        return cfOracle.getCPIndex(inst);
    }

    public final int getTotalStackSizeOfAllMethods() {
        return fmOracle.getTotalStackSizeOfAllMethods();
    }

    public final int getTotalLVSizeOfAllMethods() {
        return fmOracle.getTotalLVSizeOfAllMethods();

    }

    public final String getUTF8(int index, MultiplePoolsFacade cp) {
        return cfOracle.getUTF8(index, cp);
    }

    public final MethodInfo getInstanceInitMethod(ClassFile classFile) {
        return cfOracle.getInstanceInitMethod(classFile);
    }

    /**
     * return method classname->methodName->desription. 
     * It assumes that you are using GlobalConstantPool.
     * @param fieldOrMethod
     * @return
     */
    public final String getMethodOrFieldString(
            FieldInfo fieldOrMethod) {
        MultiplePoolsFacade pOne = fieldOrMethod.getClassFile().getConstantPool();
        String name = methodOrFieldName(fieldOrMethod, pOne);
        String desc = methodOrFieldDescription(fieldOrMethod, pOne);
        String className = fieldOrMethod.getClassFile().getFullyQualifiedClassName();
        return className.replace("/", ".") + "." + name + desc;
    }

    public final String getMethodStringShort(MethodInfo method) {
        MultiplePoolsFacade pOne = method.getClassFile().getConstantPool();
        String name = methodOrFieldName(method, pOne);
        String className = method.getClassFile().getFullyQualifiedClassName();
        if (className.contains("/")) {
            className = className.substring(className.lastIndexOf("/") + 1);
        }
        return className + "." + name;
    }

    public final FieldInfo getMethodOrField(FieldInfo field,
            boolean isMethod) {

        return cfOracle.getMethodOrField(field.getClassFile(), field,
                isMethod);
    }

    public final FieldInfo getMethodOrField(ClassFile cFile, String methodName,
            String methodDesc, boolean isMethod) {
        return cfOracle.getMethodOrField(cFile, methodName, methodDesc, isMethod);
    }

    public final FieldInfo getMethodOrField(String fullyQualifiedClassName,
            String methodName, String methodDesc, boolean isMethod) {
        return cfOracle.getMethodOrField(fullyQualifiedClassName, methodName,
                methodDesc, isMethod);
    }

    public final String methodOrFieldDescription(FieldInfo mInfo, MultiplePoolsFacade cp) {
        return cfOracle.methodOrFieldDescription(mInfo, cp);
    }

    public final String methodOrFieldName(FieldInfo mInfo, MultiplePoolsFacade cp) {
        return cfOracle.methodOrFieldName(mInfo, cp);
    }

    public final String methodOrFieldName(ReferenceInfo refInfo, MultiplePoolsFacade cp) {
        return cfOracle.methodOrFieldName(refInfo, cp);
    }

    public final String methodOrFieldDescription(ReferenceInfo refInfo, MultiplePoolsFacade cp) {
        return cfOracle.methodOrFieldDescription(refInfo, cp);
    }

    public final int getMethodInfoIndexFromContr(ClassFile cFile, MethodInfo method) {
        return cfOracle.getMethodInfoIndexFromContr(cFile, method);
    }

    //------------------- FieldMethodOracle ---------------------------
    public final boolean isEmptyContructor(MultiplePoolsFacade pOne, MethodInfo method) {
        return fmOracle.isEmptyContructor(pOne, method);
    }

    public final boolean isInstanceInitMethod(ReferenceInfo method, MultiplePoolsFacade cp) {
        return fmOracle.isInstanceInitMethod(methodOrFieldName(method, cp));
    }

    public final boolean isClassInterfaceInitMethod(MultiplePoolsFacade pOne, MethodInfo method) {
        return fmOracle.isClassInterfaceInitMethod(pOne, method);
    }

    public final boolean isInstanceInitMethod(String methodName) {
        return fmOracle.isInstanceInitMethod(methodName);
    }

    public final boolean isClassInterfaceInitMethod(ReferenceInfo method, MultiplePoolsFacade cp) {
        return fmOracle.isClassInterfaceInitMethod(methodOrFieldName(method, cp));
    }

    public final void clearMethodCodeAttAndClassFileCache() {
        fmOracle.clearCache();
    }

    public final Vector<MethodInfo> getAllMethodInfo() {
        return fmOracle.getAllMethodInfo();
    }

    public final Vector<CodeAttCache> getAllCodeAtt() {
        return fmOracle.getAllCodeAtt();
    }

    public final Vector<ExceptionAttCache> getAllExceptionAtt() {
        return fmOracle.getAllExceptionAtt();
    }

    public final void updateAllCodeAttsLength() {
        fmOracle.updateAllCodeAttsLength();
    }

    public final long getCurrentTotalCodeLength() {
        return fmOracle.getTotalCodeLength();
    }

    public final HashSet<OpcodeMnemonicInUse> getAllOpcodeMnemonicsUseInCode() {
        return fmOracle.getAllOpcodeMnemonicsUseInCode();
    }

    //------------------ End of Field Method Oracle ------------------------
    //----------------- Start of Class File Oracle ------------------------
    /**
     *
     * @param fullyQualifiedInterfaceName
     * @return
     * class files implementing the given interface
     */
    public final Vector<ClassFile> getAllDirectImplementations(String fullyQualifiedInterfaceName) {
        return cfOracle.getAllDirectImplementations(fullyQualifiedInterfaceName);
    }

    public final ClassFile getSuperClass(ClassFile cFile) {
        return cfOracle.getSuperClass(cFile);
    }

    public final void getAllSuperInterfaces(ClassFile cFile, Vector<ClassFile> interfaces) throws Exception {
        cfOracle.getAllSuperInterfaces(cFile, interfaces);
    }

    /**
     * Given an interface it returns all the interfaces extending this interface.
     * @param givenInterface
     * @param interfaces
     * @throws Exception
     */
    public final void getAllSubInterfaces(ClassFile givenInterface, Vector<ClassFile> interfaces) throws Exception {
        cfOracle.getAllSubInterfaces(givenInterface, interfaces);
    }

    /**
     * get all the interfaces implemented by the cFile or by any of its super classfiles.
     *
     * @param cFile
     * @param interfaces
     * @throws Exception
     */
    public final Vector<ClassFile> getAllInterfacesImpled(ClassFile cFile) throws Exception {
        return cfOracle.getAllInterfacesImpled(cFile);
    }

    /**
     * returns all the superclasses of cFile
     *
     * @param cFile ClassFile
     * @param retSet Vector
     * @throws Exception
     */
    public final void getAllSuperClasses(ClassFile cFile, Vector retSet) throws
            Exception {
        cfOracle.getAllSuperClasses(cFile, retSet);
    }

    public final HashSet getAllClassesStartWith(String startWith) throws Exception {
        return cfOracle.getAllClassesStartWith(startWith);
    }

    /**
     * returns all the subclasses of input class
     * The classes are ordered.
     * 
     * @param fullyQualifedName
     * @return
     * @throws java.lang.Exception
     */
    public final HashSet getAllSubClasses(String fullyQualifedName) throws Exception {
        return cfOracle.getAllSubClasses(fullyQualifedName);
    }

    /**
     * returns all the subclasses of input class
     * 
     * @param cFile
     * @return
     * @throws java.lang.Exception
     */
    public final HashSet getAllSubClasses(ClassFile cFile) throws Exception {
        return cfOracle.getAllSubClasses(cFile);
    }

    /**
     * It returns common super class of both A and B that it neareast to them.
     *
     * Note that a common super class can always be found because Object is common
     * superclass of every class.
     *
     * @param classA int
     * @param classB int
     * @return int
     */
    public final int getNearestCommonSuperClass(int classA, int classB) throws
            Exception {
        //1. check if A and B are equals then return anyone of them
        //2. Get all the superclasses of A. Say it "superASet"
        //-- 2(a): Get one superclass of B and see if that is in superASet. if so return it
        //--- Keep repeating step 3(a) and you will get one super class sooner or later for sure...
        return cfOracle.getNearestCommonSuperClass(classA, classB);
    }

    /**
     * Name will be fully qualified for example java/lang/Object
     * In case a class could not be found (may be not loaded or does not exist) then
     * the function will return -1
     * 
     * @param name
     * @param cp
     * @return 
     */
    public final int getClassInfoByName(String name, MultiplePoolsFacade cp) {
        return cfOracle.getClassInfoByName(name, cp);
    }

    /**
     * returns true if fileA is subclass of fileB
     *
     * @param fileA ClassFile
     * @param fileB ClassFile
     * @return boolean
     * @throws Exception
     */
    public final boolean isSubClass(ClassFile fileA, ClassFile fileB) throws
            Exception {
        return cfOracle.isSubClass(fileA, fileB);
    }

    /**
     * return the package of ClassFile
     * @param file ClassFile
     * @return String
     * @throws Exception
     */
    public final String getPackage(ClassFile file) throws Exception {
        return cfOracle.getPackage(file);
    }

    /**
     * Give an interface it returns all the classes implementing it
     * directly or though its any subinterface.
     *
     * @param givenInterface
     * @param implementation
     * @throws Exception
     */
    public final void getAllImplementations(ClassFile givenInterface, HashSet<ClassFile> implementation) throws Exception {
        cfOracle.getAllImplementations(givenInterface, implementation);
    }

    /**
     * returns ClassFile by using thisClass index in the Constant Pool
     * 
     * @param thisClass
     * @param cp
     * @return
     */
    public final ClassFile getClass(int thisClass, MultiplePoolsFacade cp) {
        return cfOracle.getClass(thisClass, cp);
    }

    /**
     * return class from class file controller based on fullyQualifiedName of a class file
     * @param fullyQualifiedName
     * @return
     */
    public final ClassFile getClass(String fullyQualifiedName) {
        return cfOracle.getClass(fullyQualifiedName);
    }

    /**
     * returns ClassFile by using thisClass index in the Constant Pool
     * 
     * @param thisClass
     * @param cp
     * @return
     */
    public final ClassFile getClass(Un thisClass, MultiplePoolsFacade cp) {
        return getClass(thisClass.intValueUnsigned(), cp);
    }

    /**
     * return MethodInfoController index of MethodInfo with a given name and description
     * @param cFile
     * @param methodName
     * @param methodDesc
     * @return
     */
    public final int getMethodInfoIndexFromContr(ClassFile cFile, String methodName,
            String methodDesc) {
        return cfOracle.getMethodInfoIndexFromContr(cFile, methodName, methodDesc);
    }

    public final boolean isReferenceFromClassFile(ReferenceInfo ref, ClassFile file, boolean isMethod) {
        return cfOracle.isReferenceFromClassFile(ref, file, isMethod);
    }

    /**
     *
     * @param cp
     * @param ref
     * @param file
     * @param isMethod
     * @return
     * fieldinfo controller of the class files
     */
    public final int getReferenceFieldFromClassFile(MultiplePoolsFacade cp,
            ReferenceInfo ref, ClassFile file, boolean isMethod) {
        return cfOracle.getReferenceFieldFromClassFile(cp, ref, file, isMethod);
    }

    /**
     * return controller index inside a classfile. Returns -1 if field does not 
     * exist in the class
     * @param ref
     * @param file
     * @param isMethod
     * @return
     */
    public final int getReferenceFieldFromClassFile_GCP(ReferenceInfo ref, ClassFile file, boolean isMethod) {
        return cfOracle.getReferenceFieldFromClassFile_GLOBALCP(ref, file, isMethod);
    }

    // ------------------------- Global CP Oracle --------------------
    /**
     * 
     * @param thisPointer
     * @return
     */
    public final String getClassInfoName(MultiplePoolsFacade pOne, Un thisPointer) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.getClassInfoName(pOne, thisPointer);
    }

    /**
     *
     * @param name
     * @return
     */
    public final int getUTF8InfoIndex(MultiplePoolsFacade pOne, String name) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.getUTF8InfoIndex(pOne, name);

    }

    /**
     *
     * @param fieldName
     * @param fieldDescription
     * @param cFile
     * @param isMethod
     * @return
     */
    public final int getFieldCPIndex(MultiplePoolsFacade pOne, String fieldName, String fieldDescription,
            ClassFile cFile, boolean isMethod) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.getFieldCPIndex(pOne, fieldName, fieldDescription,
                cFile, isMethod);
    }

    /**
     * adds a fields in a classfile if not already exist and also add the field in CP
     * @param fieldName
     * @param fieldDescription
     * @param cFile
     * @param isMethod
     * @param accessFlags
     * return -1 if field already exist in classFile. Otherwise return CP index of the field.
     */
    public final int addFieldInfoInClassFileAndCP(MultiplePoolsFacade pOne,
            String fieldName,
            String fieldDescription, ClassFile cFile, boolean isMethod, Un accessFlags) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.addFieldInfoInClassFileAndCP(pOne, fieldName, fieldDescription,
                cFile, isMethod, accessFlags);
    }

    /**
     * It creates a nameAndType for a field
     * @param field
     * @return
     */
    public final NameAndTypeInfo createNameAndType(FieldInfo field) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.createNameAndType(field);
    }

    public final int findNameAndType_GCP(MultiplePoolsFacade pOne, int nameIndex, int descIndex) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.findNameAndType_GCP(pOne, nameIndex, descIndex);
    }

    public final void renameFieldInfo(MultiplePoolsFacade pOne, FieldInfo fieldInfo, String newName) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();

        gcpOracle.renameFieldInfo(pOne, fieldInfo, newName);
    }

    public final void renameFieldInfo(MultiplePoolsFacade pOne, FieldInfo fieldInfo, int nameAndTypeIndex) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();

        gcpOracle.renameFieldInfo(pOne, fieldInfo, nameAndTypeIndex);
    }

    /**
     * rename fieldRefInfo
     * return nAType used in renaming
     * 
     * @param cpIndexFieldRef
     * @param newName
     * @return
     */
    public int renameFieldRefInfo(MultiplePoolsFacade pOne,
            int cpIndexFieldRef, String newName) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();

        return gcpOracle.renameFieldRefInfo(pOne, cpIndexFieldRef, newName);
    }

    public final int addReferenceInfoInCP(MultiplePoolsFacade pOne,
            ReferenceInfo refInfo, boolean isMethod) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();

        return gcpOracle.addReferenceInfoInCP(pOne, refInfo, isMethod);
    }

    /**
     * It add a fieldInfo in the CP by creating a FieldRefInfo. In case field already
     * exist then it return the index of already exisiting field.
     *       
     * @param field
     * @param isMethod
     * @param cFileIndex
     * @return
     */
    public final int addFieldInfoInCP(MultiplePoolsFacade pOne,
            FieldInfo field, boolean isMethod, int cFileIndex) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();

        return gcpOracle.addFieldorMethodInfoInCP(pOne, field, isMethod, cFileIndex);
    }

    /**
     * return -1 if the field does not exist. otherwise it index
     * Note: with interface methods this function does not work correctly
     * @param field
     * @param isMethod
     * @param cFileIndex
     * @return
     */
    public final int existFieldInfoCPIndex(MultiplePoolsFacade pOne,
            FieldInfo field, boolean isMethod, int cFileIndex) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.existFieldInfoCPIndex(pOne, field, isMethod, cFileIndex);
    }

    public final int existReferenceCPIndex(MultiplePoolsFacade pOne,
            ReferenceInfo refInfo, boolean isMethod) {
        GlobalCPOracle gcpOracle = GlobalCPOracle.getInstanceOf();
        return gcpOracle.existReferenceCPIndex(pOne, refInfo, isMethod);
    }
}
