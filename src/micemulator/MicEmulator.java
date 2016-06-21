/*
 * Copyright (C) 2016 CHARSLAB
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package micemulator;

/**
 *
 * @author carlo
 */
public class MicEmulator {
    
    public static void runTest() {
        CPURegister MAR = new CPURegister(0, "MAR");
        CPURegister MDR = new CPURegister(0, "MDR");
        CPURegister PC  = new CPURegister(0, "PC");
        CPURegister MBR = new CPURegister(0, "MBR");
        CPURegister SP  = new CPURegister(0, "SP");
        CPURegister LV  = new CPURegister(0, "LV");
        CPURegister CPP = new CPURegister(0, "CPP");
        CPURegister TOS = new CPURegister(0, "TOS");
        CPURegister OPC = new CPURegister(0, "OPC");
        CPURegister H   = new CPURegister(0, "H");
        
        Bus bus_C = new Bus();
        bus_C.addEndpoint(MAR, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(MDR, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(PC, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(MBR, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(SP, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(LV, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(CPP, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(TOS, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(OPC, CPURegister.ID_Inputs.IN_0);
        bus_C.addEndpoint(H, CPURegister.ID_Inputs.IN_0);
        
        Shifter shifter = new Shifter();
        shifter.setControlValue(Shifter.ID_Controls.SLL8, 1);
        
        Bus busAluToShifter = new Bus();
        busAluToShifter.addEndpoint(shifter, Shifter.ID_Inputs.IN_0);

        ALU32 alu = new ALU32(busAluToShifter);
        alu.configControls(ALU.f_B_NOT);
        
        Bus bus_B = new Bus();
        bus_B.addEndpoint(alu, ALU.ID_Inputs.B);
        int alu_input_b = 0;
        int expected = ~alu_input_b;
        
        bus_B.sendInput(alu_input_b);
        int result = alu.getLastResult();
        if(result != expected)
            throw new RuntimeException("ERROR: ALU output mismatch with expected result");
        
        Decoder decoder = new Decoder(4, 16);
        decoder.connectTo(MAR, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(MDR, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(PC, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(MBR, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(SP, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(LV, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(CPP, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(TOS, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(OPC, CPURegister.ID_Controls.OUT_0);
        decoder.connectTo(H, CPURegister.ID_Controls.OUT_0);
        decoder.onInputReceived(Decoder.ID_Inputs.IN_0, 1);
        
        System.out.println("Testing registers");
        System.out.println("Storing value 245");
        MAR.setControlValue(CPURegister.ID_Controls.EN_0, 1);
        MAR.setControlValue(CPURegister.ID_Controls.OUT_0, 0);
        MAR.onInputReceived(CPURegister.ID_Inputs.IN_0, 245);
        System.out.println("MAR (disabled): " + MAR.getState());
        if(MAR.getState() != 0) 
            throw new RuntimeException("ERROR: Register output expected to be 0 (disabled) but was not");
        
        MAR.setControlValue(CPURegister.ID_Controls.OUT_0, 1);
        MAR.onInputReceived(CPURegister.ID_Inputs.IN_0, 245);
        System.out.println("MAR (output enabled): " + MAR.getState());
        if(MAR.getState() != 245) 
            throw new RuntimeException("ERROR: Register state mismatch with expected state");
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        runTest();
    }
}
