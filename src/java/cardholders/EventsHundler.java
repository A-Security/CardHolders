/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardholders;

import com.asecurity.eventslogdb.AccessEvents;
import com.asecurity.eventslogdb.AccessEvents_Service;
import com.asecurity.eventslogdb.ApacseventsCha;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.xml.ws.BindingProvider;

/**
 *
 * @author vudalov
 */
public class EventsHundler {
    private final ChsCfg cfg;
    public EventsHundler (ChsCfg cfg){
        this.cfg = cfg;
    }
    
    private Map<Entry<String, String>, List<ApacseventsCha>> faultsPerHolders;

    private List<ApacseventsCha> getAccessFaults() {

        AccessEvents_Service service = new AccessEvents_Service();
        AccessEvents port = service.getAccessEventsPort();
        BindingProvider bindingProvider = (BindingProvider) port;
        try {
            URI wsurl = new URI((String) bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
            wsurl = new URI(wsurl.getScheme(), wsurl.getUserInfo(), cfg.getAShost(), wsurl.getPort(), wsurl.getPath(), wsurl.getQuery(), wsurl.getFragment());
            bindingProvider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, wsurl.toString());
        } catch (URISyntaxException ex) {
            
        }
        return port.getAccessFaults();

    }

    public void fillFaultsPerHolders() {
        this.faultsPerHolders = new HashMap<>();
        for (ApacseventsCha aec : getAccessFaults()) {
            Entry<String, String> holder = new SimpleEntry<>(aec.getHolderid(), aec.getHoldername());
            List<ApacseventsCha> list = this.faultsPerHolders.get(holder);
            if (list == null) {
                list = new ArrayList<>();
                this.faultsPerHolders.put(holder, list);
            }
            list.add(aec);
        }
    }

    /**
     * @return the faultsPerHolders
     */
    public Map<Entry<String, String>, List<ApacseventsCha>> getFaultsPerHolders() {
        return faultsPerHolders;
    }
}
