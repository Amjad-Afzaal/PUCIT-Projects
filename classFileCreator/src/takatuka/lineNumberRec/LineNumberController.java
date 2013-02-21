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
import takatuka.util.*;
import takatuka.classreader.dataObjs.attribute.*;
import takatuka.classreader.logic.util.*;
import takatuka.optimizer.bytecode.branchSetter.dataObjs.attributes.*;


/**
 * <p>Title: </p>
 * <p>Description:
 *
 * The class stores codes' line numbers per instruction id. It store those record
 * corresponding to each method.
 * The above MUST be done before even any bytecode optimization is preformed.
 *
 * </p>
 * @author Faisal Aslam
 * @version 1.0
 */
public class LineNumberController {

    private static final LineNumberController lineNumberContr = new LineNumberController();
    private TreeMap<Long, Integer> record = new TreeMap<Long, Integer>();
    private boolean enableCaching = false;

    /**
     * contructor is private
     */
    private LineNumberController() {
    }

    /**
     *
     * @return
     */
    public static LineNumberController getInstanceOf() {
        return lineNumberContr;
    }

    public int getLineNumberInfo(Instruction instr) {
        return getLineNumberInfo(instr.getInstructionId());
    }

    public void enableCaching(boolean value) {
        this.enableCaching = value;
    }
    
    /**
     * Given an instrId it return an instruction that was before it in
     * the same method and has a valid line number corresponding to it.
     * If no such instruction exist then it return first instruction of the method
     * with line number.
     * If no such instruction exist then finally it return -1;
     * @param instrId instruction whose valid line number does not exist
     * @return line Number
     */
    private int getPreviousInstrWithValidLineNumber(long instrId) {
        CodeAtt codeAtt = CodeAtt.getCodeAttForInstr(instrId);
        if (codeAtt == null) {
            return -1;
        }
        Vector instrVec = codeAtt.getInstructions();
        //System.out.println("see me "+instrVec);
        Instruction prevInstr = MethodInfo.findPreviousInstr(instrId, instrVec);
        if (prevInstr == null) {
            /**
             * The input instrId must belong to the first instruction of
             * the method and must not has a valid line number.
             * Hence first first instruction in the method with valid line number.
             */
           return getFirstInstrWithValidLineNumber(instrVec);
        }
        long prevInstrId = prevInstr.getInstructionId();
        Integer lineNumber = record.get(prevInstrId);
        if (lineNumber == null) {
            return getPreviousInstrWithValidLineNumber(prevInstrId);
        }
        return lineNumber;
    }

    private int getFirstInstrWithValidLineNumber(Vector instrVec) {
        Iterator<Instruction> it = instrVec.iterator();
        while (it.hasNext()) {
            Instruction instr = it.next();
            Integer lineNumber = record.get(instr.getInstructionId());
            if (lineNumber != null) {
                return lineNumber;
            }
        }
        return -1;
    }
    /**
     * 
     * @param instrId
     * @return
     */
    public int getLineNumberInfo(long instrId) {
        Integer lineNumber = null;
        lineNumber = record.get(instrId);
        // Miscellaneous.println("\n\n record " + record);
        if (lineNumber == null && instrId >= 0) {
            lineNumber = getPreviousInstrWithValidLineNumber(instrId);
            if (lineNumber != -1 && enableCaching) {
                record.put(instrId, lineNumber);
            }
            return lineNumber;
        } else if (lineNumber != null) {
            return lineNumber;
        }

        return -1;
    }

    /**
     * This function MUST be called before bytecode optimization.
     * That is how it works:
     * 1. Get all the code attributes from the oracle. and get a corresponding
     *    Line number attribute.
     * 2. 
     */
    public void storeLineNumbers() {
        Oracle oracle = Oracle.getInstanceOf();
        oracle.clearMethodCodeAttAndClassFileCache();
        Vector<CodeAttCache> codeAttCache = oracle.getAllCodeAtt();
        Iterator<CodeAttCache> codeAttCacheIt = codeAttCache.iterator();
        while (codeAttCacheIt.hasNext()) {
            CodeAttCache codeAttCacheObj = (CodeAttCache) codeAttCacheIt.next();
            //Miscellaneous.println("--a--a--a " + codeAttCacheObj.getClassFile().getFullyQualifiedClassName());
            LineNumberTableAtt lineNumberAtt = getLineNumberAttribute((CodeAtt) codeAttCacheObj.getAttribute());
            if (lineNumberAtt != null) {
                storeLineNumbers(lineNumberAtt, (MethodInfo) codeAttCacheObj.getMethodInfo());
            }
        }
        //Miscellaneous.println("----------------- " + toString());
    }

    /**
     *
     * @param codeAtt
     * @return
     */
    public static LineNumberTableAtt getLineNumberAttribute(CodeAtt codeAtt) {
        AttributeInfoController attContr = codeAtt.getAttributes();
        LineNumberTableAtt ret = null;
        for (int loop = 0; loop < attContr.getCurrentSize(); loop++) {
            AttributeInfo attInfo = (AttributeInfo) attContr.get(loop);
            if (attInfo instanceof LineNumberTableAtt) {
                if (ret != null) {
                    Miscellaneous.println("There could be multiple line number attributes. Exiting...");
                    Miscellaneous.exit();
                }
                ret = (LineNumberTableAtt) attInfo;
            }
        }
        return ret;

    }

    private Instruction findInstructionOriginalOffSet(int offSet, Vector<Instruction> instrs) throws Exception {
        Instruction ret = null;

        for (int loop = 0; loop < instrs.size(); loop++) {
            ret = instrs.elementAt(loop);
            if (ret.getOriginalOffSet() == offSet) {
                return ret;
            }
        }
        throw new Exception("Invalid instruction (or Jump) address :" + offSet);
        /*        throw new VerifyErrorExt(
        "Invalid instruction (or Jump) address :" + offSet);
         */
    }

    /**
     * 
     * @param lineNumberAtt
     * @param method
     */
    private void storeLineNumbers(LineNumberTableAtt lineNumberAtt, MethodInfo method) {
        try {
            Vector instrVec = method.getInstructions();
            for (int loop = 0; loop < lineNumberAtt.getLineNumberTableLength(); loop++) {
                int lineNumber = lineNumberAtt.getLineNumber(loop);
                int offSet = lineNumberAtt.getStartPC(loop).intValueUnsigned();
                BHInstruction instr = null;
                try {
                    instr = (BHInstruction) findInstructionOriginalOffSet(offSet, instrVec);
                } catch (Exception d) {
                    Miscellaneous.exit();
                }
                if (instr == null) {
                    Miscellaneous.printlnErr("Error # 6950");
                    Miscellaneous.exit();
                }
                record.put(instr.getInstructionId(), lineNumber);
            }
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
    }

    @Override
    public String toString() {
        return record.toString();
    }
}
