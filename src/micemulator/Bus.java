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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author carlo
 */
public class Bus {
    private List<BusInputListener> endpoints;
    private List<Integer> inputsId; 
    
    private static int numBus = 0;
    private int busId;
    
    public Bus() {
        endpoints = new ArrayList<>();
        inputsId = new ArrayList<>();
        
        busId = numBus++;
    }
    
    public int getId() {
        return busId;
    }
    
    public void addEndpoint(BusInputListener listener, int inputId) {
        endpoints.add(listener);
        inputsId.add(inputId);
    }
   
    
    public void sendInput(int value) {
        //if(value.length != endpoints.size())
        //    throw new IllegalArgumentException("Invalid input size");
        
        for(int i = 0; i < endpoints.size(); i++)
                endpoints.get(i).onInputReceived(inputsId.get(i), value);
    }
}
