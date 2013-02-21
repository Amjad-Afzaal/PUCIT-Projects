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
package frameSizeCalc.logic;

/**
 *
 *
 * @author Faisal Aslam
 */
public class ParameterSizeCalc {

    public static void main(String args[]) {
        String methodDesc = "ISCLJava/Lang/Object;B[[[DI[LJava/String;F";
        int result = ParameterSizeCalc.getParameterSize(methodDesc, false);
    }
    /*
     * B 	byte 	signed byte
    C 	char 	Unicode character
    D 	double 	double-precision floating-point value
    F 	float 	single-precision floating-point value
    I 	int 	integer
    J 	long 	long integer
    L<classname>; 	reference 	an instance of class <classname>
    S 	short 	signed short
    Z 	boolean 	true or false
    [ 	reference 	one array dimension 
     */

    public static int getParameterSize(String methodDesc, boolean isStatic) {
        int size = 0;
        if (!isStatic) {
            size++; //extra variable for this pointer
        }
        methodDesc = methodDesc.substring(1, methodDesc.indexOf(")"));
        for (int charIndex = 0; charIndex < methodDesc.length(); charIndex++) {
            char c = methodDesc.charAt(charIndex);
            if (c == 'B' || c == 'C' || c == 'F' || c == 'I' || c == 'S'
                    || c == 'Z' || c == 'L' || c == '[') {
                size++;
                if (c == 'L') {
                    charIndex = skipReference(methodDesc, charIndex);
                } else if (c == '[') {
                    charIndex = skipArray(methodDesc, charIndex);
                }
            } else if (c == 'D' || c == 'J') {
                size++;
            } else {
                throw new UnsupportedOperationException();
            }
        }
        return size;
    }

    private static int skipReference(String methodDesc, int currentIndex) {
        String reference = methodDesc.substring(currentIndex);
        reference = reference.substring(0, reference.indexOf(";"));
        return currentIndex + reference.length();
    }

    private static int skipArray(String methodDesc, int currentIndex) {
        int nextIndex = currentIndex;
        String toSkip = methodDesc.substring(currentIndex);
        int toSkipSize = 1;
        while (toSkip.charAt(toSkipSize) == '[') {
            toSkipSize++;
        }
        if (toSkip.charAt(toSkipSize) == 'L') {
            toSkipSize = skipReference(toSkip, toSkipSize);
        }
        return nextIndex + toSkipSize;
    }
}
