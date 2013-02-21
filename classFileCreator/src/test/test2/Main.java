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
package test.test2;

/**
 *
 *
 * @author Faisal Aslam
 */
public class Main {

    public static void main(String args[]) {
        int i = 1;
        int k = 5;
        i = i + k;
        System.out.println("It is working");
        if (i == 0) {
            ClassTwo classTwo = new ClassTwo();
            System.out.println("It is working 2");
            classTwo.foo();
        }
    }

    private void foobar() {
        int j = 3;
        int k = 3 * 19;
        System.out.println("I should be deleted");

    }

    private void foo() {
        System.out.println("I should NOT be deleted");
    }
}



