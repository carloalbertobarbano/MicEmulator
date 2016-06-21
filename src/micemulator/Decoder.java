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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class Decoder extends InputReceiver implements BusInputListener {
    private int in_size;
    private int out_size;
    
    private List<InputReceiver> endpoints;
    private List<Integer> controlsId;
    
    public class ID_Inputs {
        public static final int IN_0 = 0x00;
    }
    
    public Decoder(int in_size, int out_size) {
        this.in_size = in_size;
        this.out_size = out_size;
        super.addInput(ID_Inputs.IN_0, 0);
        
        endpoints = new ArrayList<>();
        controlsId = new ArrayList<>();
    }
    
    public void connectTo(InputReceiver endpoint, Integer controlId) {
        endpoints.add(endpoint);
        controlsId.add(controlId);
    }
    
    @Override
    public void onInputReceived(int inputId, int input) {
        if(input > Math.pow(2, in_size))
            throw new IllegalArgumentException("Decoder input bigger than in_size");
        
        System.out.println("Decoder input: " + input);
        System.out.print("Decoder output: ");
        for(int i = 0; i < endpoints.size(); i++) {
            System.out.print(i == input-1 ? 1 : 0);
            endpoints.get(i).setControlValue(controlsId.get(i), i == input ? 1 : 0);
        }
        System.out.println("");
    }
    
    
}
