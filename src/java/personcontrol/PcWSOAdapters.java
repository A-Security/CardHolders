package personcontrol;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.datacontract.schemas._2004._07.apacsadapter.*;
import org.wso2.carbon.registry.core.*;

public class PcWSOAdapters {
    private static final String ARTIFACT_PATH = "/_system/governance";
    private static final String HOLDERS_PATH = "/ssoi/cardholders";
    private static final String VIPS_PATH = "/ssoi/personcontrol";
    private static final String HOLDERS_FULL_PATH = ARTIFACT_PATH + HOLDERS_PATH;
    private static final String VIPS_FULL_PATH = ARTIFACT_PATH + VIPS_PATH;
    private static final String GR_HOST = "192.168.0.151";
    private static final int GR_PORT = 9443;
    private static final String GR_USER = "admin";
    private static final String GR_PASS = "Flvbybcnhfnjh1";
    private URL grUrl;
    private Registry registry;
    private List<AdpCardHolder> vipCHs;
    private List<AdpCardHolder> notVipCHs;
    public PcWSOAdapters(){
        try {
            grUrl = new URL("https", GR_HOST, GR_PORT, "/registry");
            registry = new RemoteRegistry(grUrl, GR_USER, GR_PASS);
            fillCHsSets();
        } catch (Exception ex) { 
            ex.printStackTrace();
        }
    }
    public boolean fillGRCardHolders() {
        AdpWebService service = new AdpWebService();
        AdpWebServiceSoap port = service.getAdpWebServiceSoap();
        return port.fillGRCardHolders();
    }
    
    private void fillCHsSets() {
        vipCHs = new ArrayList<>();
        notVipCHs = new ArrayList<>();
        try {
            Collection col = (Collection) registry.get(HOLDERS_FULL_PATH);
            for (String resPath : col.getChildren()) {
                Resource res = registry.get(resPath);
                if (res != null && !(res instanceof Collection)) {
                    AdpCardHolder ch = new AdpCardHolder(res.getContentStream());
                    if (ch.isVip()) {
                        getVipCHs().add(ch);
                    }
                    else {
                        getNotVipCHs().add(ch);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<AdpCardHolder> getVipCHs() {
        return vipCHs;
    }

    public List<AdpCardHolder> getNotVipCHs() {
        return notVipCHs;
    }
}
