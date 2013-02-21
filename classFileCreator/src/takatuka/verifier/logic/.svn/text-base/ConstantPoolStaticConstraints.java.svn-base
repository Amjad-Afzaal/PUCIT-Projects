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

import java.util.*;

import takatuka.classreader.dataObjs.*;
import takatuka.classreader.dataObjs.constantPool.*;
import takatuka.classreader.dataObjs.constantPool.base.*;
import takatuka.classreader.logic.constants.*;
import takatuka.verifier.logic.exception.*;

/**
 * <p>Title: </p>
 * <p>Description:
 * Here we verify all the static constraints of constant pool
 * Following are the constraints that we check here.
 * 1. In MethodRefInfo, FieldRefInfo and InterfaceRefInfo each
 *   class_index point to a classInfo, each nameAndTypeIndex points to nameAndTypeIndex.
 * 2. Each staring_index in a StringInfo point to a valid UTF8Info
 * 3. Each NameAndTypeInfo's name_index and descriptor_index point to UTF8Infos
 * 4. In UTF8Info length is equal to bytes array size.
 * 5. ClassInfo's name_index point to a UTF8Info.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class ConstantPoolStaticConstraints {

    private static final ConstantPoolStaticConstraints cpSC =
            new ConstantPoolStaticConstraints();

    private ConstantPoolStaticConstraints() {
    }

    public static ConstantPoolStaticConstraints getInstanceOf() {
        return cpSC;
    }


    /**
     * It executes all the constaints on the given Constant Pool
     *
     * @param cp ConstantPool
     */
    public void execute(MultiplePoolsFacade pOne) throws Exception { //1
        InfoBase cpValue = null;
        TreeSet poolIds = pOne.getPoolIds();
        Iterator poolIdsIt = poolIds.iterator();
        while (poolIdsIt.hasNext()) {
            int poolId = (Integer)poolIdsIt.next();
            
        for (int loop = 1; loop < pOne.getCurrentSize(poolId); loop++) {
            cpValue = (InfoBase)pOne.get(loop, poolId);
            if ((getTag(cpValue) == CPTagValues.CONSTANT_Methodref ||
                 getTag(cpValue) == CPTagValues.CONSTANT_Fieldref ||
                 getTag(cpValue) == CPTagValues.CONSTANT_InterfaceMethodref) &&
                !verifyReferences(pOne, (ReferenceInfo) cpValue)) {
                throw new VerifyErrorExt(Messages.INVALID_CONSTANT_POOL + loop, false);
            } else if (getTag(cpValue) == CPTagValues.CONSTANT_String &&
                       !verifyStringInfo(pOne, (StringInfo) cpValue)) {
                throw new VerifyErrorExt(Messages.INVALID_CONSTANT_POOL + loop, false);
            } else if (getTag(cpValue) == CPTagValues.CONSTANT_NameAndType &&
                       !verifyNameAndType(pOne, (NameAndTypeInfo) cpValue)) {
                throw new VerifyErrorExt(Messages.INVALID_CONSTANT_POOL + loop, false);
            } else if (getTag(cpValue) == CPTagValues.CONSTANT_Utf8 &&
                       !verifyUTF8((UTF8Info) cpValue)) {
                throw new VerifyErrorExt(Messages.INVALID_CONSTANT_POOL + loop, false);
            } else if (getTag(cpValue) == CPTagValues.CONSTANT_Class &&
                       !verifyClassInfo(pOne, (ClassInfo) cpValue)) {
                throw new VerifyErrorExt(Messages.INVALID_CONSTANT_POOL + loop, false);
            }
        }
        }
    }

    private int getTag(InfoBase base) throws Exception {
        return base.getTag().intValueUnsigned();
    }

    private InfoBase get(MultiplePoolsFacade pOne, int index, int poolId) throws Exception {
        return (InfoBase) pOne.get(index, poolId);
    }

    private InfoBase get(MultiplePoolsFacade pOne, Un index, int poolId) throws Exception {
        return get(pOne, index.intValueUnsigned(), poolId);
    }

    //step 1
    private boolean verifyReferences(MultiplePoolsFacade pOne,
            ReferenceInfo rInfo) throws Exception {
        if (get(pOne, rInfo.getIndex(), CPTagValues.CONSTANT_Class) instanceof ClassInfo &&
            get(pOne, rInfo.getNameAndTypeIndex(), CPTagValues.CONSTANT_NameAndType)
            instanceof NameAndTypeInfo) {
            return true;
        }
        return false;
    }

    //step 2
    private boolean verifyStringInfo(MultiplePoolsFacade pOne, StringInfo rInfo) throws Exception {
        if (get(pOne, rInfo.getIndex(), CPTagValues.CONSTANT_Utf8) instanceof UTF8Info) {
            return true;
        }
        return false;
    }

    //step 3
    private boolean verifyNameAndType(MultiplePoolsFacade pOne, NameAndTypeInfo nAt) throws Exception {
        if (get(pOne, nAt.getIndex(), CPTagValues.CONSTANT_Utf8) instanceof UTF8Info &&
            get(pOne, nAt.getDescriptorIndex(), CPTagValues.CONSTANT_Utf8) instanceof UTF8Info) {
            return true;
        }
        return false;
    }

    //step 4
    private boolean verifyUTF8(UTF8Info utf8) throws Exception {
        if (utf8.getLength().intValueUnsigned() == utf8.getBytes().size()) {
            return true;
        }
        return false;
    }

    //step 5
    private boolean verifyClassInfo(MultiplePoolsFacade pOne, ClassInfo classInfo) throws Exception {
        if (get(pOne, classInfo.getIndex(), CPTagValues.CONSTANT_Utf8) instanceof UTF8Info) {
            return true;
        }
        return false;
    }

}
