/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardholders;

import com.asecurity.esbdb.AccessEvents;
import com.asecurity.esbdb.AccessEvents_Service;
import com.asecurity.esbdb.ApacseventsCha;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author vudalov
 */
public class EventsHundler {

    private Map<Entry<String, String>, List<ApacseventsCha>> faultsPerHolders;
    private final String resPath = "http://192.168.0.151:9763/registry/resource/";
    private final String grPhotoPath = "_system/governance/ssoi/cardholders/photo/";
    private String photoPath = resPath + grPhotoPath;
    

    public EventsHundler() {
    }

    private List<ApacseventsCha> getAccessFaults() {
        AccessEvents_Service service = new AccessEvents_Service();
        AccessEvents port = service.getAccessEventsPort();
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

    /**
     * @return the photoPath
     */
    public String getPhotoPath() {
        return photoPath;
    }
}
