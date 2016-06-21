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
public class CPURegister extends InputReceiver implements BusInputListener{
    private static int numRegisters = 0;
    private String name;
    
    public class ID_Inputs {
        final static int IN_0 = 0x00;
    }
    
    public class ID_Controls {
        final static int EN_0 = 0x00;
        final static int OUT_0 = 0x01;
    }
    
    public CPURegister() {
        this(0, "Register_" + CPURegister.numRegisters);
    }
    
    public CPURegister(int state) {
        this(state, "Register_" + CPURegister.numRegisters);
    }
    
    public CPURegister(String name) {
        this(0, name);
    }
    
    public CPURegister(int state, String name) {
        super();
        super.addControl(ID_Controls.EN_0);
        super.setControlValue(ID_Controls.EN_0, 1);
        super.addControl(ID_Controls.OUT_0);
        super.setControlValue(ID_Controls.OUT_0, 1);
        
        super.addInput(ID_Inputs.IN_0, state);
        
        this.name = name;
        CPURegister.numRegisters++;
    }
    
    public int getState() {
        return getControlValue(ID_Controls.OUT_0) == 1 ? getInputValue(ID_Inputs.IN_0) : 0;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    @Override
    public void onInputReceived(int inputId, int input) {
        if(this.getControlValue(ID_Controls.EN_0) == 1) {
            System.out.println("Received input on " + name + ": " + input + " on input: " + inputId);
            this.inputs.put(inputId, input);
        }
    }
}
