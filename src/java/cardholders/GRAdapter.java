package cardholders;

import java.io.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.w3c.dom.*;
import org.wso2.carbon.governance.api.cache.ArtifactCacheManager;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.xml.sax.SAXException;

public class GRAdapter {
    //private static final String GREG_HOME = "/opt/wso2/gr";
    private static final String ARTIFACT_PATH = "/_system/governance";
    private static final String HOLDERS_PATH = "/ssoi/cardholders";
    private static final String VIPS_PATH = "/ssoi/personcontrol";
    private static final String HOLDERS_FULL_PATH = ARTIFACT_PATH + HOLDERS_PATH;
    private static final String VIPS_FULL_PATH = ARTIFACT_PATH + VIPS_PATH;
    private final String GR_HOST;
    private static final int GR_PORT = 9443;
    private static final String GR_USER = "Apacs";
    private static final String GR_PASS = "Aa1234567";
    private URL grUrl;
    private Registry registry;
    private List<AdpCardHolder> vipCHs = new ArrayList<>();
    private List<AdpCardHolder> notVipCHs = new ArrayList<>();
    private DocumentBuilderFactory xDocBuilder = DocumentBuilderFactory.newInstance();
    private DocumentBuilder builder;
    private final ChsCfg cfg;
    
    public GRAdapter(ChsCfg cfg) {
        this.cfg = cfg;
        this.GR_HOST = cfg.getGRhost();
        System.setProperty("carbon.repo.write.mode", "true");
        try {
            this.xDocBuilder.setNamespaceAware(true);
            this.xDocBuilder.setValidating(false);
            this.builder = this.xDocBuilder.newDocumentBuilder();
            this.grUrl = new URL("https", GR_HOST, GR_PORT, "/registry");
            this.registry = new RemoteRegistry(grUrl, GR_USER, GR_PASS);            
        } catch (MalformedURLException | RegistryException | ParserConfigurationException e) {
            out.print(e.toString());
        } 
        ArtifactCacheManager.enableClientCache();
        
    }
    public AdpCardHolder getAchById(String holderid){
        
        AdpCardHolder ch = null;
        String chPath = HOLDERS_FULL_PATH + "/" + holderid + ".xml";
        try {
            Resource res = registry.get(chPath);
            ch = new AdpCardHolder(res.getContentStream(), builder);
        } catch (RegistryException ex) {
            System.out.println(ex.toString());
            if (ch == null){
                ch = new AdpCardHolder();
            }
        }
        return ch;
    }
    public boolean fillCHLists() {
        boolean result;
        vipCHs.clear();
        notVipCHs.clear();
        try {
            Collection col = (Collection) registry.get(HOLDERS_FULL_PATH);
            for (String resPath : col.getChildren()) {
                Resource res = registry.get(resPath);
                if (res != null && !(res instanceof Collection)) {
                    AdpCardHolder ch = new AdpCardHolder(res.getContentStream(), builder);
                    String vipPath = VIPS_FULL_PATH + "/" +ch.getId();
                    boolean vipExist = registry.resourceExists(vipPath);
                    if (vipExist ^ ch.isVip()) {
                        res = setVipValue(res, vipExist);
                        registry.put(resPath, res);
                    }
                    if (vipExist){
                        vipCHs.add(ch);
                    }
                    else {
                        notVipCHs.add(ch);
                    }
                }
            }
            result = true;
        } catch (Exception ex) {
            out.print(ex.toString());
            result = false;
        }
        return result;
    }
    private Resource setVipValue(Resource res, boolean vipVal) {
        byte[] content;
        try {
            Document xdoc = builder.parse(res.getContentStream());
            Node vip = xdoc.getElementsByTagName("vip").item(0);
            if (vip != null && (vipVal ^ Boolean.valueOf(vip.getTextContent()))) {
                vip.setTextContent(String.valueOf(vipVal));
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                DOMSource source = new DOMSource(xdoc);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                StreamResult sr = new StreamResult(baos);
                transformer.transform(source, sr);
                content = baos.toByteArray();
                if (content != null){
                    res.setContent(content);
                    res.setProperty("holder_vip", String.valueOf(vipVal));
                }
            }
        } catch (RegistryException | TransformerException | SAXException | IOException | DOMException e) {
            out.print(e.toString());
        }
        return res;
    }
    public boolean setCHsVipValue(String[] chsList, boolean vipValue) {
        boolean result = false;
        if (chsList == null || chsList.length == 0){
            return result;
        }
        try {
            for (String ch : chsList) {
                String chPath = HOLDERS_FULL_PATH + "/" + ch + ".xml";
                String vipPath = VIPS_FULL_PATH + "/" + ch;
                if (registry.resourceExists(vipPath)){
                    if (!vipValue){
                       registry.delete(vipPath); 
                    }
                }
                else{
                    if (vipValue){
                        registry.put(vipPath, registry.newResource());
                    }
                }
                if (registry.resourceExists(chPath)) {
                    Resource res = registry.get(chPath);
                    res = setVipValue(res, vipValue);
                    registry.put(chPath, res);
                }
            }
            result = true;
        } catch (Exception ex) {
            out.print(ex.toString());
            result = false;
        }
        return result;
    }

    public List<AdpCardHolder> getVipList() {
        Collections.sort(vipCHs);
        return vipCHs;
    }

    public List<AdpCardHolder> getNotVipList() {
        Collections.sort(notVipCHs);
        return notVipCHs;
    }
}
