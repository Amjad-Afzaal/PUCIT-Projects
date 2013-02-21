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

import takatuka.classreader.logic.constants.FieldTypes;

/**
 *
 *
 * @author Faisal Aslam
 */
public class FieldType {

    private byte type;
    private String className = "java/lang/Object";
    private int dimensions = 1;
    private byte primitiveType;
    public FieldType(byte type) {
        this.type = type;
    }

    /**
     * Used both by the reference type and by the reference arrays
     * @param className 
     */
    public void addClassName(String className) {
        this.className = className;
    }
    
    /**
     * 
     * @param dimensions Number of dimensions for an array 
     */
    public void addDimensions(int dimensions) {
        this.dimensions = dimensions;
    }
    
    /**
     * Use to set the primitive type if the first
     * type was an array.
     * @param type 
     */
    public void addPermitiveType(byte type) {
        this.primitiveType = type;
    }
    
    private String primitiveTypes(byte ptype) {
        if (ptype == FieldTypes.BOOLEAN) {
            return "Z";
        } else if (ptype == FieldTypes.BYTE) {
            return "B";
        } else if (ptype == FieldTypes.CHAR) {
            return "C";
        } else if (ptype == FieldTypes.DOUBLE) {
            return "D";
        } else if (ptype == FieldTypes.FLOAT) {
            return "F";
        } else if (ptype == FieldTypes.INTEGER) {
            return "I";
        } else if (ptype == FieldTypes.LONG) {
            return "J";
        } else if (ptype == FieldTypes.SHORT) {
            return "S";
        
        } else 
            return null;
    }
    @Override
    public String toString() {
        String ret = primitiveTypes(type);
        if (ret != null) {
            return ret;
        } else if (type == FieldTypes.REF) {
            return "L"+className+";";
        } else if (type == FieldTypes.ARRAY) {
            ret = "";
            for (int loop = 0; loop < dimensions; loop ++) {
                ret += "[";
            }
            if (className!=null) {
            ret += "L"+className+";";
            } else {
                ret += primitiveTypes(primitiveType);
            }
            return ret;
        } else if (type == FieldTypes.VOID) {
            return "V";
        } else {
            throw new UnsupportedOperationException();
        }
    }
}
