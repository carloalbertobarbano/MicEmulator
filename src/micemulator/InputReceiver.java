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

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author carlo
 */
public class InputReceiver {
    protected HashMap<Integer, Integer> inputs;
    private HashMap<Integer, Integer> controls; 
    
    public InputReceiver() {
        inputs = new HashMap<>();
        controls = new HashMap<>();
    }
    
    public void addInput(int id, int value) {
        inputs.put(id, value);
    }
    
    public int getInputValue(int id) {
        return inputs.get(id);
    }
    
    public void addControl(int id) {
        controls.put(id, 1);
    }
    
    /*public void enableInput(int id, int enable) {
        if(inputs.get(id) == null)
            throw new IllegalArgumentException("Invalid input id: " + id);
        inputs.put(id, enable >= 1);
    }*/
    
    public void setControlValue(int id, int value) {
        if(controls.get(id) == null)
            throw new IllegalArgumentException("Invalid control id: " + id);
        controls.put(id, value);
    }
    
    public Integer getControlValue(int id) {
        return controls.get(id);
    }
    
    public void configControls(int config[]) {
        if(config.length != controls.size())
            throw new IllegalArgumentException("Invalid config param");
        
        Object keys[] = controls.keySet().toArray();
        for(int i = 0;i < config.length; i++) {
            controls.put((Integer)keys[i], config[i]);
        }
    }
    
    public int[] getControlsConfig() {
        int config[] = new int[controls.size()];
        
        Object keys[] = controls.keySet().toArray();
        for(int i = 0; i < config.length; i++)
            config[i] = controls.get((Integer)keys[i]);
       
        return config;
    }
    
    public boolean controlsMatchConfig(int config[]) {
        if(config.length != controls.size())
            return false;
        
        Object keys[] = controls.keySet().toArray();
        
        for(int i = 0; i < config.length; i++) {
            //System.out.println("controlKeys: " + (Integer)keys[i]);
            if(controls.get((Integer)keys[i]) != config[i])
                return false;
        }
  
        return true;
    }
}
