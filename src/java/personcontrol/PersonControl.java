package personcontrol;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.datacontract.schemas._2004._07.apacsadapter.*;
import org.wso2.carbon.registry.core.*;

public class PersonControl {
    public boolean fillGRCardHolders() {
        AdpWebService service = new AdpWebService();
        AdpWebServiceSoap port = service.getAdpWebServiceSoap();
        return port.fillGRCardHolders();
    }
    public ArrayList<AdpCardHolder> getCHsFromGR() {
        ArrayList<AdpCardHolder> holders = new ArrayList<AdpCardHolder>();
        String ARTIFACT_PATH = "/_system/governance";
        String HOLDERS_PATH = "/ssoi/cardholders";
        String VIPS_PATH = "/ssoi/personcontrol";
        String holdersFullPath = ARTIFACT_PATH + HOLDERS_PATH;
        String VIPsFullPath = ARTIFACT_PATH + VIPS_PATH;
        String url = "https://192.168.0.151:9443/registry";
        String username = "admin";
        String password = "Flvbybcnhfnjh1";
        System.setProperty("carbon.repo.write.mode", "true");
        try {
            Registry registry = new RemoteRegistry(new URL(url), username, password);
            Collection col = (Collection)registry.get(holdersFullPath);
            for (String res : col.getChildren()) {
                Resource resrc = registry.get(res);
                if (resrc != null && !(resrc instanceof Collection)) {
                    InputStream is = resrc.getContentStream();
                    holders.add(new AdpCardHolder(is));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return holders;
    }
}
