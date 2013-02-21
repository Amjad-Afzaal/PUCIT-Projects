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
import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.logic.constants.*;
import takatuka.classreader.logic.factory.*;
import takatuka.classreader.logic.util.*;

/**
 *
 * Description:
 * <p>
 *
 * It has all the utility function to add field and methods in the class files
 * to the constant pool.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
class GlobalCPOracle {
    
    private FactoryFacade factory =
            FactoryPlaceholder.getInstanceOf().getFactory();
    //private GlobalConstantPool pOne = GlobalConstantPool.getInstanceOf();
    private static final GlobalCPOracle cfileToCP = new GlobalCPOracle();
    
    private GlobalCPOracle() {
    }
    
    final static GlobalCPOracle getInstanceOf() {
        return cfileToCP;
    }

    /**
     * It add a fieldInfo in the CP by creating a FieldRefInfo. In case field already
     * exist then it return the nameIndex of already exisiting field.
     *       
     * @param field
     * @param isMethod
     * @param cFileIndex
     * @return
     * return -1 if the field already exist in CP otherwise, return the index of newly added field.
     */
    final int addFieldorMethodInfoInCP(MultiplePoolsFacade pOne, FieldInfo field, boolean isMethod, int cFileIndex) {
        //UTF8Info name = (UTF8Info)pOne.get(field.getNameIndex().intValueUnsigned(), CPTagValues.CONSTANT_Utf8);
        //Miscellaneous.println("name of method/field ="+name);

        int fieldAlreadyInCP = existFieldInfoCPIndex(pOne, field, isMethod, cFileIndex);
        //UTF8Info nameUtf8 = (UTF8Info) pOne.get(field.getNameIndex().intValueUnsigned(), CPTagValues.CONSTANT_Utf8);

        if (fieldAlreadyInCP != -1) {
            return -1; //already there. No need to be added
        }
        ReferenceInfo refInfo = createReferenceInfo(pOne, field, isMethod, cFileIndex);
        if (field.getAccessFlags().isStatic() && (refInfo instanceof FieldRefInfo)) {
            ((FieldRefInfo) refInfo).isStatic = true;
        }
        return addReferenceInfoInCP(pOne, refInfo, isMethod);
    }

    /**
     * adds a fields in a classfile if not already exist and also add the field in CP
     *
     * @param fieldName
     * @param fieldDescription
     * @param cFile
     * @param isMethod
     * @param accessFlags
     * return -1 if field already exist in classFile. Otherwise return CP index of the field.
     */
    final int addFieldInfoInClassFileAndCP(MultiplePoolsFacade pOne, String fieldName, String fieldDescription,
            ClassFile cFile, boolean isMethod, Un accessFlags) {
        FieldInfo field = Oracle.getInstanceOf().getMethodOrField(cFile, fieldName,
                fieldDescription, isMethod);
        if (field != null) {
            return -1;
        }
        int nameIndex = checkAndAddUTF8(pOne, fieldName);
        int descIndex = checkAndAddUTF8(pOne, fieldDescription);
        field = addFieldorMethodInfoInClassFile(nameIndex, descIndex, cFile, isMethod, accessFlags);
        int cfileIndex = cFile.getThisClass().intValueUnsigned();
        int ret = existFieldInfoCPIndex(pOne, field, isMethod, cfileIndex);
        if (ret == -1) {
            ret = addFieldorMethodInfoInCP(pOne, field, isMethod, cfileIndex);
        }
        Oracle.getInstanceOf().clearMethodCodeAttAndClassFileCache();
        
        return ret;
    }
    
    final int getFieldCPIndex(MultiplePoolsFacade pOne, String fieldName, String fieldDescription,
            ClassFile cFile, boolean isMethod) {
        FieldInfo field = Oracle.getInstanceOf().getMethodOrField(cFile, fieldName,
                fieldDescription, isMethod);
        if (field == null) {
            return -1;
        }
        int cfileIndex = cFile.getThisClass().intValueUnsigned();
        return existFieldInfoCPIndex(pOne, field, isMethod, cfileIndex);
    }
    
    private final FieldInfo addFieldorMethodInfoInClassFile(int nameCPIndex,
            int descrCPIndex, ClassFile cFile, boolean isMethod, Un accessFlags) {
        FieldInfo field = null;
        try {
            field = factory.createFieldInfo(cFile);
            if (isMethod) {
                field = factory.createMethodInfo(cFile);
            }
            field.setDescriptorIndex(factory.createUn(descrCPIndex).trim(2));
            field.setNameIndex(factory.createUn(nameCPIndex).trim(2));
            field.setAttributeCount(factory.createUn(0).trim(2));
            field.setAccessFlags(accessFlags);
            ControllerBase contr = cFile.getMethodInfoController();
            if (!isMethod) {
                contr = cFile.getFieldInfoController();
            }
            int maxSize = contr.getMaxSize();
            contr.setMaxSize(maxSize + 1);
            contr.add(field);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return field;
    }
    
    final String getClassInfoName(MultiplePoolsFacade pOne, Un thisPointer) {
        ClassInfo cInfo = (ClassInfo) pOne.get(thisPointer.intValueUnsigned(), CPTagValues.CONSTANT_Class);
        int classNameIndex = cInfo.getIndex().intValueUnsigned();
        UTF8Info utf8 = (UTF8Info) pOne.get(classNameIndex, CPTagValues.CONSTANT_Utf8);
        return utf8.convertBytes();
    }
    
    final int getUTF8InfoIndex(MultiplePoolsFacade pOne, String name) {
        int size = pOne.getCurrentSize(CPTagValues.CONSTANT_Utf8);
        for (int loop = 0; loop < size; loop++) {
            UTF8Info utf8Info = (UTF8Info) pOne.get(loop, CPTagValues.CONSTANT_Utf8);
            if (utf8Info.convertBytes().equals(name)) {
                return loop;
            }
        }
        return -1;
    }
    
    private final int addUTF8Info(MultiplePoolsFacade pOne, String name) {
        try {
            UTF8Info utf8 = factory.createUTF8Info();
            byte[] data = name.getBytes();
            utf8.setBytes(factory.createUn(data));
            utf8.setLength(factory.createUn(data.length).trim(2));
            return pOne.add(utf8, CPTagValues.CONSTANT_Utf8);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return -1;
    }
    
    private int checkAndAddUTF8(MultiplePoolsFacade pOne, String newName) {
        int index = getUTF8InfoIndex(pOne, newName);
        if (index == -1) {
            index = addUTF8Info(pOne, newName);
        }
        return index;
    }
    
    private int checkAndAddNameAndTypeInfo(MultiplePoolsFacade pOne, int nameIndex, int descriptionIndex) {
        try {
            Un descIndex = factory.createUn(descriptionIndex).trim(2);
            return checkAndAddNameAndTypeInfo(pOne, nameIndex, descIndex);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return -1;
    }
    
    private int checkNameAndTypeInfoInCP(MultiplePoolsFacade pOne, NameAndTypeInfo nAT) {
        int size = pOne.getCurrentSize(CPTagValues.CONSTANT_NameAndType);
        for (int loop = 0; loop < size; loop++) {
            NameAndTypeInfo nATypeInfo = (NameAndTypeInfo) pOne.get(loop, CPTagValues.CONSTANT_NameAndType);
            if (nATypeInfo.equals(nAT)) {
                return loop;
            }
        }
        return -1;
    }
    
    private int checkAndAddNameAndTypeInfo(MultiplePoolsFacade pOne, int nameIndex, Un descriptionIndex) {
        NameAndTypeInfo nAt = createNameAndType(nameIndex, descriptionIndex);
        int nAtIndex = checkNameAndTypeInfoInCP(pOne, nAt);
        if (nAtIndex == -1) {
            nAtIndex = addNameAndType(pOne, nAt);
        }
        return nAtIndex;
    }
    
    final void renameFieldInfo(MultiplePoolsFacade pOne, FieldInfo fieldInfo, int nameAndTypeIndex) {
        try {
            NameAndTypeInfo nAt = (NameAndTypeInfo) pOne.get(nameAndTypeIndex,
                    CPTagValues.CONSTANT_NameAndType);
            fieldInfo.setNameIndex(nAt.getIndex());
            fieldInfo.setDescriptorIndex(nAt.getDescriptorIndex());
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }
    
    final void renameFieldInfo(MultiplePoolsFacade pOne, FieldInfo fieldInfo, String newName) {
        int nameAndTypeIndex = checkAndAddNameAndTypeInfo(pOne, checkAndAddUTF8(pOne, newName), fieldInfo.getDescriptorIndex());
        renameFieldInfo(pOne, fieldInfo, nameAndTypeIndex);
    }

    /**
     * rename fieldRefInfo
     * return nameAndType Index
     * @param cpIndexFieldRef
     * @param newName
     * @return
     */
    final int renameFieldRefInfo(MultiplePoolsFacade pOne, int cpIndexFieldRef, String newName) {
        //first check if newName is already in CP
        //if No then add it
        //now check if nameAndType with new name and old description is in the CP
        //if No then add it
        //now change the nameAndType of the current entry of the constant pool
        //also rename the corresponding FieldInfo
        int nAtIndex = 0;
        if (cpIndexFieldRef == -1) {
            return -1;
        }
        try {
            int nameIndex = checkAndAddUTF8(pOne, newName);
            FieldRefInfo fRef = (FieldRefInfo) pOne.get(cpIndexFieldRef,
                    CPTagValues.CONSTANT_Fieldref);
            NameAndTypeInfo nAt = (NameAndTypeInfo) pOne.get(fRef.getNameAndTypeIndex().intValueUnsigned(),
                    CPTagValues.CONSTANT_NameAndType);
            nAtIndex = checkAndAddNameAndTypeInfo(pOne, nameIndex,
                    nAt.getDescriptorIndex());
            fRef.setNameAndType(factory.createUn(nAtIndex).trim(2));
            
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return nAtIndex;
    }
    
    final int addReferenceInfoInCP(MultiplePoolsFacade pOne, ReferenceInfo refInfo, boolean isMethod) {
        
        int ret = -1;
        try {
            int poolId = CPTagValues.CONSTANT_Fieldref;
            if (isMethod) {
                poolId = CPTagValues.CONSTANT_Methodref;
            }
            //following code need a review.
            Un thisClass = refInfo.getIndex();
            ClassFile.currentClassToWorkOn = Oracle.getInstanceOf().getClass(thisClass, pOne);
            ret = pOne.add(refInfo, poolId);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return ret;
    }

    /**
     * return -1 if the field does not exist. otherwise it nameIndex
     * @param field
     * @param isMethod
     * @param cFileIndex
     * @return
     */
    final int existFieldInfoCPIndex(MultiplePoolsFacade pOne, FieldInfo field, boolean isMethod, int cFileIndex) {
        int ret = -1;
        try {
            int nAtIndex = getNameAndType(pOne, field);
            if (nAtIndex == -1) {
                return -1;
            }
            
            ReferenceInfo refInfo = factory.createFieldRefInfo();
            if (isMethod) {
                refInfo = factory.createMethodRefInfo();
            }
            refInfo.setNameAndType(factory.createUn(nAtIndex).trim(2));
            refInfo.setIndex(factory.createUn(cFileIndex).trim(2));
            return existReferenceCPIndex(pOne, refInfo, isMethod);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return ret;
    }
    
    final int existReferenceCPIndex(MultiplePoolsFacade pOne, ReferenceInfo refInfo, boolean isMethod) {
        int tag = isMethod ? CPTagValues.CONSTANT_Methodref : CPTagValues.CONSTANT_Fieldref;
        return pOne.getAll(tag).indexOf(refInfo);
    }

    /**
     * In case a nameAndType already exist then it returns it otherwise,
     * it return -1
     * @param field
     * @return
     */
    private int getNameAndType(MultiplePoolsFacade pOne, FieldInfo field) {
        NameAndTypeInfo nameAndType = createNameAndType(field);
        int tag = CPTagValues.CONSTANT_NameAndType;
        return pOne.getAll(tag).indexOf(nameAndType);
    }

    /**
     *
     * @param nameIndex
     * @param descIndex
     * @return
     */
    final int findNameAndType_GCP(MultiplePoolsFacade pOne, int nameIndex, int descIndex) {
        int size = pOne.getCurrentSize(CPTagValues.CONSTANT_NameAndType);
        for (int loop = 0; loop < size; loop++) {
            NameAndTypeInfo nAT = (NameAndTypeInfo) pOne.get(loop, CPTagValues.CONSTANT_NameAndType);
            int nATNameIndex = nAT.getIndex().intValueUnsigned();
            int nATDescIndex = nAT.getDescriptorIndex().intValueUnsigned();
            if (nATNameIndex == nameIndex && nATDescIndex == descIndex) {
                return loop;
            }
        }
        return -1;
    }
    
    private NameAndTypeInfo createNameAndType(int nameIndex, Un uDescIndex) {
        NameAndTypeInfo nameAndType = null;
        try {
            Un uNameIndex = factory.createUn(nameIndex).trim(2);
            nameAndType = factory.createNameAndTypeInfo();
            nameAndType.setDescriptorIndex(uDescIndex);
            nameAndType.setIndex(uNameIndex);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return nameAndType;
    }

    /**
     * It creates a nameAndType for a field
     * @param field
     * @return
     */
    final NameAndTypeInfo createNameAndType(FieldInfo field) {
        NameAndTypeInfo nameAndType = null;
        try {
            Un uNameIndex = field.getNameIndex();
            Un uDescIndex = field.getDescriptorIndex();
            nameAndType = factory.createNameAndTypeInfo();
            nameAndType.setDescriptorIndex(uDescIndex);
            nameAndType.setIndex(uNameIndex);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return nameAndType;
    }
    
    private int addNameAndType(MultiplePoolsFacade pOne, NameAndTypeInfo nAt) {
        int ret = -1;
        try {
            ret = pOne.add(nAt, CPTagValues.CONSTANT_NameAndType);
        } catch (Exception d) {
            d.printStackTrace();
            System.exit(1);
        }
        return ret;
        
    }
    
    private int addNameAndType(MultiplePoolsFacade pOne, FieldInfo field) {
        int ret = getNameAndType(pOne, field);
        if (ret != -1) {
            return ret; //already there no need to be added
        }
        NameAndTypeInfo nAt = createNameAndType(field);
        return addNameAndType(pOne, nAt);
    }
    
    private ReferenceInfo createReferenceInfo(MultiplePoolsFacade pOne, 
            FieldInfo field, boolean isMethod, int classRefCPIndex) {
        ReferenceInfo refInfo = null;
        try {
            if (isMethod) {
                refInfo = factory.createMethodRefInfo();
            } else {
                refInfo = factory.createFieldRefInfo();
            }
            int nAtIndex = getNameAndType(pOne, field);
            if (nAtIndex == -1) {
                nAtIndex = addNameAndType(pOne, field);
                
            }
            refInfo.setNameAndType(factory.createUn(nAtIndex).trim(2));
            refInfo.setIndex(factory.createUn(classRefCPIndex).trim(2));
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return refInfo;
    }
    
    private void recordUnSortedIndexes(Vector vec, HashMap map) {
        for (int loop = 0; loop < vec.size(); loop++) {
            map.put(vec.elementAt(loop), loop);
        }
    }
}
