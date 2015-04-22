/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardholders;

import com.asecurity.esbdb.AccessEvents;
import com.asecurity.esbdb.AccessEvents_Service;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vudalov
 */
public class EventsHundler {

    public List<ApacseventsCha> getAccessFaults() {
        AccessEvents_Service service = new AccessEvents_Service();
        AccessEvents port = service.getAccessEventsPort();
        List<ApacseventsCha> res = new ArrayList<>();
        List<Object> af = port.getAccessFaults();
        for (Object f : af) {
            res.add((ApacseventsCha)f);
        }
        return res;
    }
    
}
