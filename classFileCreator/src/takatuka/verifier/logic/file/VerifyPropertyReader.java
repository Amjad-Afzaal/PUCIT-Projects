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
package takatuka.verifier.logic.file;

import java.util.*;

import takatuka.classreader.logic.file.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * @author Faisal Aslam
 * @version 1.0
 */

public class VerifyPropertyReader extends PropertyReader {
    private final static VerifyPropertyReader propertyReader = new
            VerifyPropertyReader();
    private Properties verfyProp = null;
    private final static String FILE_NAME = "verificationMessages.properties";
    private VerifyPropertyReader() {
        super();
    }


    public static VerifyPropertyReader getInstanceOf() {
        return propertyReader;
    }

    /*private Properties loadProperties() {
        return verfyProp = super.loadProperties(FILE_NAME);
         }


     public void getAllObjectToBeRemoved(Vector toRemove) {
         Vector keys = new Vector(remvProp.keySet());
         String key = null;

         for (int loop = 0; loop < keys.size(); loop++) {
        key = (String) keys.elementAt(loop);
        if (remvProp.get(key).equals(REMOVE)) {
            toRemove.addElement(manipulateKey(key));
        }
         }
     }


     public void loadAllProperties() {
         loadRemoverProperties();
         loadRemoverHelperProperties();
     }

     public static void main(String args[]) {
         // Read properties file.
         Properties pro = PropertyReaderRemover.getInstanceOf().
                     loadRemoverHelperProperties();
         Vector keys = new Vector(pro.keySet());
         Miscellaneous.println(StringUtil.getString(keys, "\n"));
     }
     */
}
