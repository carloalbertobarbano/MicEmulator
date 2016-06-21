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
public class Shifter extends InputReceiver implements BusInputListener{
    private Bus outBus; 
    
    public class ID_Controls {
        public static final int SLL8 = 0x00;
        public static final int SRA1 = 0x01;
    }
    
    public class ID_Inputs {
        public static final int IN_0 = 0x00;
    }
    public Shifter() {
        this(null);
    }
    
    public Shifter(Bus outBus) {
        this.outBus = outBus;
        super.addControl(ID_Controls.SLL8);
        super.addControl(ID_Controls.SRA1);
        super.setControlValue(ID_Controls.SLL8, 0);
        super.setControlValue(ID_Controls.SRA1, 0);
    }
    
    public void setOutBus(Bus outBus) {
        this.outBus = outBus;
    }
    
    @Override
    public void onInputReceived(int inputId, int input) {
       int result = 0;
       
       if(getControlValue(ID_Controls.SLL8) == 1) {
           System.out.println("Shifter - SLL8 selected");
           result = input << 8;
           
       } else if(getControlValue(ID_Controls.SRA1) == 1) {
           System.out.println("Shifter - SRA1 selected");
           result = input >> 1;
           
       } else {
           System.out.println("Shifter - passthrough");
           result = input;
       }
       
       System.out.println("Input: " + Integer.toString(input, 2) + ", output: " + Integer.toString(result, 2));
       
       if(outBus != null)
           outBus.sendInput(result);
    }
       
}
