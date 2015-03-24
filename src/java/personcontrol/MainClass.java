package personcontrol;
//import org.wso2.carbon.registry.app.RemoteRegistry;
//import org.wso2.carbon.governance.api.util.GovernanceUtils;
import org.datacontract.schemas._2004._07.apacsadapter.*;

public class MainClass {
    public static boolean fillGRCardHolders() {
        AdpWebService service = new AdpWebService();
        AdpWebServiceSoap port = service.getAdpWebServiceSoap();
        return port.fillGRCardHolders();
    }
    public void gr(){
        String url = "https://localhost:9443/registry";
        String username = "admin";
        String password = "admin";
        System.setProperty("carbon.repo.write.mode", "true");
        //Registry rootRegistry = new RemoteRegistry(new URL(url), username, password);
        //Registry governanceRegistry = GovernanceUtils.getGovernanceUserRegistry(rootRegistry, username);
        //GovernanceUtils.loadGovernanceArtifacts((UserRegistry) govReg);
        //return govReg;
    }
}
