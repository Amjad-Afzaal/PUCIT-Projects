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
package takatuka.lineNumberRec;

import java.util.*;
import takatuka.classreader.dataObjs.MethodInfo;
import takatuka.classreader.dataObjs.attribute.Instruction;
import takatuka.optimizer.bytecode.branchSetter.dataObjs.attributes.BHInstruction;

/**
 * <p>Title: </p>
 * <p>Description:
 * 
 * After bytecode optimization (just before writing a TUK file) the above information
 * is used to generate a property file with offset=linenumberFrom-lineNumberTo data.
 * Note that we have to generate to-from line numbers as we combine many line numbers.
 * In case an instruction is not combined then it will have only offset=linenumber instead
 * of to-from.
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
class LineNumberPropertyFile {

    private static LineNumberPropertyFile lineNumberHandler = new LineNumberPropertyFile();
    private static StringBuffer buffer = new StringBuffer();
    private static StringBuffer bufferDebugger = new StringBuffer();
    private static final String PROPERTY_FILE_NAME = "lineNumber.properties";
    private static final String PROPERTY_FOR_DEBUGGER_FILE_NAME = "lineNumberDebugger.properties";
    private LineNumberController lineNumberContr = LineNumberController.getInstanceOf();

    /**
     * the constructor is private
     */
    private LineNumberPropertyFile() {
    }

    /**
     * 
     * @return
     */
    public static final LineNumberPropertyFile getInstanceOf() {
        return lineNumberHandler;
    }

    /**
     * - For each method get the All the instructions.
     * - For each instructionId find the offset using LineNumberConroller.
     * - Find the offset of this instruction
     * - Write in the propertyfile methodId,offset=lineNumber(s)
     * - Note 1: In case the line number does for an instruction does not exist
     * then we have to use line number of previous instruction.
     * - Note 2: As we combine many instruction during bytecode optimization hence
     * it is possible that an instruction has multiple line number corresponding to it.
     * 
     * @param method
     */
    public void execute(MethodInfo method) {
        Vector instrVec = method.getInstructions();
        Instruction.setAllOffsets(instrVec);
        Iterator<Instruction> it = instrVec.iterator();
        int oldLineNumber = -1;
        if (buffer.length() == 0) {
            buffer.append("#methodId,offSet=lineNumber(s)\n");
        }
        lineNumberContr.enableCaching(true);
        while (it.hasNext()) {

            Instruction instr = it.next();
            TreeSet<Integer> myLineNumbers = new TreeSet<Integer>();
            int offSet = instr.getOffSet();
            String property = /*method.getReferenceIndex() +*/ "," + offSet + "=";
            Vector<BHInstruction> simpleInstrVec = new Vector<BHInstruction>();

            simpleInstrVec.add((BHInstruction) instr);

            for (int loop = 0; loop < simpleInstrVec.size(); loop++) {
                BHInstruction simpleInstr = simpleInstrVec.elementAt(loop);
                int lineNumber = lineNumberContr.getLineNumberInfo(simpleInstr.getInstructionId());
                if (lineNumber != -1) {
                    myLineNumbers.add(lineNumber);
                    oldLineNumber = lineNumber;
                } else if (oldLineNumber != -1) {
                    myLineNumbers.add(oldLineNumber);
                }
            }
            if (myLineNumbers.size() > 1) {
                //buffer.append("#" + simpleInstrVec + "\n");
            }
            String lineNumberStr = "";
            Iterator<Integer> lineNumberIt = myLineNumbers.iterator();
            while (lineNumberIt.hasNext()) {
                lineNumberStr += lineNumberIt.next();
                if (lineNumberIt.hasNext()) {
                    lineNumberStr += ",";
                }
            }
            buffer.append(property + lineNumberStr + "\n");
            bufferDebugger.append(/*method.getReferenceIndex() + */"," + lineNumberStr + "=" + offSet + "\n");

        }
    }

    /**
     *
     */
    public void writeInPropertyFile() {
        //Todo MethodNamesPropertyFile.writeInPropertyFile(PROPERTY_FILE_NAME, buffer);
        // todo MethodNamesPropertyFile.writeInPropertyFile(PROPERTY_FOR_DEBUGGER_FILE_NAME, bufferDebugger);
    }
}
