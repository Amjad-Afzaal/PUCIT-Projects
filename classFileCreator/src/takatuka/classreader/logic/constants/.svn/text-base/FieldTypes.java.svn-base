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
package takatuka.classreader.logic.constants;

import java.util.*;
import takatuka.classreader.logic.file.*;

public class FieldTypes {

    private static final String TYPES_FOR_CASTING_PROPERTY = "types.properties";
    private static final Properties typesForCastingProperties = PropertyReader.getInstanceOf().loadProperties(TYPES_FOR_CASTING_PROPERTY);
    private static final String TYPE_JCHAR_STR = "TYPE_JCHAR";
    private static final String TYPE_JBOOLEAN_STR = "TYPE_JBOOLEAN";
    private static final String TYPE_JBYTE_STR = "TYPE_JBYTE";
    private static final String TYPE_JSHORT_STR = "TYPE_JSHORT";
    private static final String TYPE_JREF_STR = "TYPE_JREF";
    private static final String TYPE_JINT_STR = "TYPE_JINT";
    private static final String TPYE_JFLOAT_STR = "TPYE_JFLOAT";
    private static final String TYPE_JDOUBLE_STR = "TYPE_JDOUBLE";
    private static final String TYPE_JLONG_STR = "TYPE_JLONG";
    private static final String TYPE_JARRAY_STR = "TYPE_JARRAY";
    private static final String TYPE_JVOID_STR = "TYPE_JVOID";
    private static final String TYPE_UNUSED = "TYPE_UNUSED";
    private static final String TYPE_SPECIAL_TAIL = "TYPE_SPECIAL_TAIL";
    private static final String TYPE_RET_ADDRESS = "TYPE_RET_ADDRESS";
    private static final String TYPE_STRING = "TYPE_STRING";
    
    public static final byte BYTE = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JBYTE_STR));
    public static final byte CHAR = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JCHAR_STR));
    public static final byte DOUBLE = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JDOUBLE_STR));
    public static final byte FLOAT = Byte.parseByte(typesForCastingProperties.getProperty(TPYE_JFLOAT_STR));
    public static final byte INTEGER = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JINT_STR));
    public static final byte LONG = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JLONG_STR));
    public static final byte REF = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JREF_STR));
    public static final byte SHORT = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JSHORT_STR));
    public static final byte BOOLEAN = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JBOOLEAN_STR));
    public static final byte ARRAY = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JARRAY_STR));
    public static final byte VOID = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_JVOID_STR));
    public static final byte UNUSED = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_UNUSED));
    public static final short SPECIAL_TAIL = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_SPECIAL_TAIL));
    public static final short RETURN_ADDRESS = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_RET_ADDRESS));
    public static final short STRING = Byte.parseByte(typesForCastingProperties.getProperty(TYPE_STRING));
    public static final short NULL = Short.MAX_VALUE;

    protected FieldTypes() {
    }
}
