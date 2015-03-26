package personcontrol;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.datacontract.schemas._2004._07.apacsadapter.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.*;
import org.xml.sax.SAXException;



public class MainClass {
    public boolean fillGRCardHolders() {
        AdpWebService service = new AdpWebService();
        AdpWebServiceSoap port = service.getAdpWebServiceSoap();
        return port.fillGRCardHolders();
    }
    public ArrayList<AdpCardHolder> getCHsFromGR() {
        ArrayList<AdpCardHolder> holders = new ArrayList<AdpCardHolder>();
        try {
            String ARTIFACT_PATH = "/_system/governance";
            String HOLDERS_PATH = "/ssoi/cardholders";
            String VIPS_PATH = "/ssoi/personcontrol";
            String holdersFullPath = ARTIFACT_PATH + HOLDERS_PATH;
            String VIPsFullPath = ARTIFACT_PATH + VIPS_PATH;
            String url = "https://192.168.0.151:9443/registry";
            String username = "admin";
            String password = "Flvbybcnhfnjh1";
            System.setProperty("carbon.repo.write.mode", "true");
            Registry registry = new RemoteRegistry(new URL(url), username, password);
            Collection col = (Collection)registry.get(holdersFullPath);
            for (String res : col.getChildren())
            {
                Resource resrc = registry.get(res);
                
                if (resrc != null)
                {
                    InputStream is = resrc.getContentStream();
                    holders.add(new AdpCardHolder(is));
                }
            }
            return holders;
        } catch (RegistryException | MalformedURLException ex) {
            return null;
        }
        
    }
    /*private AdpCardHolder GRcontentToCH(byte[] GRcontent){
        AdpCardHolder ch = new AdpCardHolder();
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            f.setValidating(false);
            DocumentBuilder builder = f.newDocumentBuilder();
            Document xdoc = builder.parse(new ByteArrayInputStream(GRcontent));
            Node idNode = xdoc.getElementsByTagName("id").item(0);
            Node shortNameNode = xdoc.getElementsByTagName("shortName").item(0);
            Node nameNode = xdoc.getElementsByTagName("name").item(0);
            Node cardNoNode = xdoc.getElementsByTagName("cardNo").item(0);
            Node photoLinkNode = xdoc.getElementsByTagName("photoLink").item(0);
            Node vipNodeNode = xdoc.getElementsByTagName("vip").item(0);
            ch.setId(idNode.getTextContent());
            ch.setShortName(shortNameNode.getTextContent());
            ch.setName(nameNode.getTextContent());
            ch.setCardNo(cardNoNode.getTextContent());
            ch.setPhotoLink(photoLinkNode.getTextContent());
            ch.setVIP(idNode.getTextContent());
        } catch (ParserConfigurationException | SAXException | IOException ex) {
        } 
        return ch;
    }*/
}
