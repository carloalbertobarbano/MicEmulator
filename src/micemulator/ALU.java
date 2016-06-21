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
public class ALU extends InputReceiver implements BusInputListener {
    protected Bus outBus;
    
    protected int input_a;
    protected int input_b; 
    
    protected int N; 
    protected int Z;
    protected int carryOut;
    protected int lastResult;
    
    /** ALU Functions:                                  F0 F1 ENA ENB INVA INC*/
    static public final int f_A[]                  = { 0,  1,  1,  0,  0,  0 };
    static public final int f_B[]                  = { 0,  1,  0,  1,  0,  0 };
    static public final int f_A_NOT[]                 = { 0,  1,  1,  0,  1,  0 };
    static public final int f_B_NOT[]                 = { 1,  0,  1,  1,  0,  0 };
    static public final int f_A_plus_B[]           = { 1,  1,  1,  1,  0,  0 };
    static public final int f_A_plus_B_plus_1[]    = { 1,  1,  1,  1,  0,  1 };
    static public final int f_A_plus_1[]           = { 1,  1,  1,  0,  0,  1 };
    static public final int f_B_plus_1[]           = { 1,  1,  0,  1,  0,  1 };
    static public final int f_B_minus_A[]          = { 1,  1,  1,  1,  1,  1 };
    static public final int f_B_minus_1[]          = { 1,  1,  0,  1,  1,  0 };
    static public final int f_A_NOT_2[]              = { 1,  1,  1,  0,  1,  1 };
    static public final int f_A_AND_B[]            = { 0,  0,  1,  1,  0,  0 };
    static public final int f_A_OR_B[]             = { 0,  1,  1,  1,  0,  0 };
    static public final int f_0[]                  = { 0,  1,  0,  0,  0,  0 };
    static public final int f_1[]                  = { 1,  1,  0,  0,  0,  1 };
    static public final int f_minus_1[]            = { 1,  1,  0,  0,  1,  0 };
    
    public class ID_Controls {
        final static int F_0   = 0x00;
        final static int F_1   = 0x01;
        final static int EN_A  = 0x02;
        final static int EN_B  = 0x03;
        final static int INV_A = 0x04;
        final static int INC   = 0x05;
    }
    
    public class ID_Inputs {
        final static int A = 0x01;
        final static int B = 0x02;
    }
    
    public ALU() {
        this(null);
    }
    
    public ALU(Bus outBus) {
        super(); 
        
        this.input_a = 0;
        this.input_b = 0;
        this.carryOut = 0;
        this.lastResult = 0;
        
        this.outBus = outBus;
        super.addControl(ID_Controls.F_0);
        super.addControl(ID_Controls.F_1);
        super.addControl(ID_Controls.EN_A);
        super.addControl(ID_Controls.EN_B);
        super.addControl(ID_Controls.INV_A); 
        super.addControl(ID_Controls.INC);
    }
    
    public void setOutBus(Bus outBus) {
        this.outBus = outBus;
    }
    
    public int getN() { return N; }
    public int getZ() { return Z; }
    public int getCarryOut() { return carryOut; }
    public int getLastResult() { return lastResult; }
    
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
        
        int result = execute();
        
        if(outBus != null)
            outBus.sendInput(result);
        
    }
    
    public int execute() {
        boolean A = input_a >= 1;
        boolean B = input_b >= 1;
        boolean F0 = getControlValue(ID_Controls.F_0) >= 1;
        boolean F1 = getControlValue(ID_Controls.F_1) >= 1;
        boolean EN_A = getControlValue(ID_Controls.EN_A) >= 1;
        boolean EN_B = getControlValue(ID_Controls.EN_B) >= 1;
        boolean INV_A = getControlValue(ID_Controls.INV_A) >= 1;
        boolean INC = getControlValue(ID_Controls.INC) >= 1;
        
        boolean b_res = ((((A && EN_A) ^ INV_A) && (B && EN_B)) && (!F0 && !F1)) || 
                 ((((A && EN_A) ^ INV_A) || (B && EN_B)) && (!F0 && F1)) || 
                 (!(B && EN_B) && (F0 && !F1)) ||
                 (((((A && EN_A) ^ INV_A) ^ (B && EN_B)) ^ INC) && (F0 && F1));
        
        int result = b_res ? 1 : 0; 
        
        //Compute outCarry
        //Formula: !ABC!S + A!BC!S + AB!C!S + ABCD
        boolean sum = A ^ B;
        boolean carry = (!A && B && INC && sum) || (A && !B && INC && !sum) || (A && B && !INC && !sum) || (A && B && INC && sum);
        carryOut = carry ? 1 : 0;
         
        Z = ~result;
        N = result < 0 ? 1 : 0;
        
        this.lastResult = result;
        return result;
    }
}
