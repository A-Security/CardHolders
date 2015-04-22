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
import net.java.dev.jaxb.array.AnyTypeArray;

/**
 *
 * @author vudalov
 */
public class EventsHundler {

    public List<ApcFaultEvents> getAccessFaults() {
        AccessEvents_Service service = new AccessEvents_Service();
        AccessEvents port = service.getAccessEventsPort();
        List<AnyTypeArray> af = port.getAccessFaults();
        List<ApcFaultEvents> eventsList = new ArrayList<>();
        for (AnyTypeArray ata : af) {
            Object[] objV = ata.getItem().toArray();
            ApcFaultEvents afe = new ApcFaultEvents(objV);
            eventsList.add(afe);
        }
        return eventsList;
    }
    
}
