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
package takatuka.classreader.dataObjs.attribute;

import java.util.*;
import org.apache.commons.lang.builder.*;
import takatuka.classreader.dataObjs.*;
import takatuka.classreader.logic.exception.*;
import takatuka.classreader.logic.factory.*;
import takatuka.io.*;
import takatuka.classreader.logic.util.*;
import takatuka.optimizer.bytecode.branchSetter.dataObjs.attributes.BHInstruction;

/**
 * 
 * Description:
 * <p>
 * </p> 
 * @author Faisal Aslam
 * @version 1.0
 */
public class ExceptionTableEntry implements BaseObject {

    private Un start_pc;
    private Un end_pc;
    private Un handler_pc;
    private Un catch_type;
    private FactoryFacade factory = FactoryPlaceholder.getInstanceOf().getFactory();
    private long handlerPCInstId = -1;
    private long startPCInstId = -1;
    private long endPCInstId = -1;

    ExceptionTableEntry(Un start_pc, Un end_pc, Un handler_pc,
            Un catch_type) throws UnSizeException, Exception {
        Un.validateUnSize(2, start_pc);
        Un.validateUnSize(2, end_pc);
        Un.validateUnSize(2, handler_pc);

        factory.createValidateUn().validateConsantPoolIndex(catch_type);
        this.start_pc = start_pc;
        this.end_pc = end_pc;
        this.handler_pc = handler_pc;
        this.catch_type = catch_type;
    }

    ExceptionTableEntry(long startPCInstrId, long endPCInstrId,
            long handlerPcInstrId, int catchType) throws Exception {
        this.startPCInstId = startPCInstrId;
        this.endPCInstId = endPCInstrId;
        this.handlerPCInstId = handlerPcInstrId;
        this.catch_type = factory.createUn(catchType).trim(2);
    }

        void savePCInformation(CodeAtt myCodeAtt) {
        Vector<BHInstruction> instsVec = myCodeAtt.getInstructions();
        if (instsVec == null || instsVec.size() == 0) {
            return;
        }
        int size = instsVec.size();
        if (!(instsVec.elementAt(0) instanceof BHInstruction)) {
            return;
        }
        for (int index = 0; index < size; index++) {
            BHInstruction inst = instsVec.elementAt(index);
            if (inst.getOffSet() == handler_pc.intValueUnsigned()) {
                handlerPCInstId = inst.getInstructionId();
                inst.setShouldOnlyComeAtTheBeginning();
            }
            if (inst.getOffSet() == start_pc.intValueUnsigned()) {
                startPCInstId = inst.getInstructionId();
                inst.setShouldOnlyComeAtTheBeginning();
            }
            if (inst.getOffSet() == end_pc.intValueUnsigned()) {
                endPCInstId = inst.getInstructionId();
                //inst.setShouldOnlyComeAtTheEnd();
                inst.setShouldOnlyComeAtTheBeginning();
            }
            if (handlerPCInstId != -1 && endPCInstId != -1 && startPCInstId != -1) {
                break;
            }
        }
    }

    private void updatePCInformation(long instId, int offSet) throws Exception {
        if (instId == handlerPCInstId) {
            handler_pc = factory.createUn(offSet).trim(2);
        }
        if (instId == startPCInstId) {
            start_pc = factory.createUn(offSet).trim(2);
        }
        if (instId == endPCInstId) {
            end_pc = factory.createUn(offSet).trim(2);
        }

    }

    void restorePCInformation(Instruction inst) throws Exception {
        updatePCInformation(inst.getInstructionId(), inst.getOffSet());
    }

    public long getHandlerPCInstrId() {
        return handlerPCInstId;
    }

    public long getStartPCInstrId() {
        return startPCInstId;
    }

    public long getEndPCInstrId() {
        return endPCInstId;
    }

    public Un getStartPC() {
        return start_pc;
    }

    public Un getEndPC() {
        return end_pc;
    }

    public Un getHandlerPC() {
        return handler_pc;
    }

    public Un getCatchType() {
        return catch_type;
    }

    public void setCatchType(Un catch_type) throws Exception {
        FactoryFacade fac = FactoryPlaceholder.getInstanceOf().
                getFactory();
        fac.createValidateUn().validateConsantPoolIndex(catch_type);
        this.catch_type = catch_type;
    }

    @Override
    public String toString() {
        String ret = "";
        try {
            ret = ret + writeSelected(null);
        } catch (Exception d) {
            d.printStackTrace();
            Miscellaneous.exit();
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ExceptionsAtt)) {
            return false;
        }
        ExceptionTableEntry att = (ExceptionTableEntry) obj;
        if (super.equals(att) && end_pc.equals(att.end_pc) && start_pc.equals(att.start_pc) && handler_pc.equals(att.handler_pc) && catch_type.equals(att.catch_type)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(start_pc).
                append(end_pc).append(handler_pc).append(catch_type).
                append(super.hashCode()).toHashCode();
    }

    @Override
    public String writeSelected(BufferedByteCountingOutputStream buff) throws
            Exception {
        String ret = "";
        ret += "start_pc =" + start_pc.writeSelected(buff);
        ret += ", end_pc =" + end_pc.writeSelected(buff);
        ret += ", handler_pc =" + handler_pc.writeSelected(buff);
        boolean debug = false;
        if (debug) {
            ret += ", handler pc instrId =" + handlerPCInstId;
            //ret += ", line number =" + LineNumberController.getInstanceOf().getLineNumberInfo(handlerPCInstId);
        }
        ret += ", catch_type =" + catch_type.writeSelected(buff);
        return ret;
    }
}
