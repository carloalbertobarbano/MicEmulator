/*
 * Copyright (C) 2016 carlo
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
public class ALU32 extends ALU {
    private ALU alu[];
    
    public ALU32() {
        this(null);
    }
    
    public ALU32(Bus outBus) {
        super(outBus);
        alu = new ALU[32];
        
        for(int i = 0; i < 32; i++)
            alu[i] = new ALU();
        //    Bus bus = new Bus();
        //    alu[i].setOutBus(bus);
        //}
    }

    @Override
    public void onInputReceived(int inputId, int input) {
        switch(inputId) {
            case ID_Inputs.A:
                this.input_a = input;
                break;
                
            case ID_Inputs.B:
                this.input_b = input;
                break;
                
            default: break;
        }
        
        if(controlsMatchConfig(f_A))
            System.out.println("ALU - Selected function: A");
        else if (controlsMatchConfig(f_B)) 
            System.out.println("ALU - Selected function: B");
        
        else if (controlsMatchConfig(f_A_NOT))
            System.out.println("ALU - Selected function: !A");
        
        else if (controlsMatchConfig(f_B_NOT))
            System.out.println("ALU - Selected function: !B");
            
        else if (controlsMatchConfig(f_A_plus_B)) 
            System.out.println("ALU - Selected function: A + B");
                    
        else if (controlsMatchConfig(f_A_plus_B_plus_1))
            System.out.println("ALU - Selected function: A + B + 1");
            
        else if (controlsMatchConfig(f_A_plus_1))
            System.out.println("ALU - Selected function: A + 1");
            
        else if (controlsMatchConfig(f_B_plus_1))
            System.out.println("ALU - Selected function: B + 1");
            
        else if (controlsMatchConfig(f_B_minus_A))
            System.out.println("ALU - Selected function: B - A");
            
        else if (controlsMatchConfig(f_B_minus_1))
            System.out.println("ALU - Selected function: B - 1");
            
        else if (controlsMatchConfig(f_A_NOT_2))
            System.out.println("ALU - Selected function: !A (2)");
            
        else if (controlsMatchConfig(f_A_AND_B))
            System.out.println("ALU - Selected function: A AND B");
            
        else if (controlsMatchConfig(f_A_OR_B))
            System.out.println("ALU - Selected function: A OR B");
            
        else if (controlsMatchConfig(f_0))
            System.out.println("ALU - Selected function: 0");
            
        else if (controlsMatchConfig(f_1))
            System.out.println("ALU - Selected function: 1");
            
        else if (controlsMatchConfig(f_minus_1))
            System.out.println("ALU - Selected function: -1");
            
        else
            System.out.println("ALU - Uknown function selected");
        
        
        int result = 0;
        int carry = getControlValue(ALU.ID_Controls.INC);
        
        System.out.println("Executing ALU. A: " + input_a + " B: " + input_b);
        System.out.println("Shifted: " + Integer.toString((input_b << 31) >>> 31, 2));
        
        int rshift = 31;
        
        for(int i = 0; i < 32; i++) {
            int lshift = 31 - i;
           
            alu[i].configControls(this.getControlsConfig());
            alu[i].setControlValue(ALU.ID_Controls.INC, carry);
            alu[i].onInputReceived(ALU.ID_Inputs.A, (input_a << lshift) >>> rshift);
            alu[i].onInputReceived(ALU.ID_Inputs.B, (input_b << lshift) >>> rshift);
            
            int res = alu[i].execute() << i;
            System.out.println("Bit " + i + ": " + Integer.toString(res, 2));
            
            result += res;
            
            carry = alu[i].getCarryOut();
        }
        carryOut = carry;
        
        Z = result == 0 ? 1 : 0;
        N = result < 0 ? 1 : 0;
        
        System.out.println("Final result (binary):");
        //System.out.println("A: " + input_a + ", B: " + input_b + " - result: " + result + " carryOut: " + carryOut);
        System.out.println("A: " + Integer.toString(input_a, 2) + " (" + input_a + "), B: " + Integer.toString(input_b, 2) + 
                            " (" + input_b + ") " + " - result: " + Integer.toString(result, 2) + 
                            " (" + result + ") " + "carryOut: " + Integer.toString(carryOut, 2) + " (" + carryOut + ")");
        
        this.lastResult = result;
        
        if(outBus != null)
            outBus.sendInput(result);
    }
    
    
}
 