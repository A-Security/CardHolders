package personcontrol;

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

public class PcWSOAdapters {
    private static final String GREG_HOME = "/opt/wso2/gr";
    private static final String ARTIFACT_PATH = "/_system/governance";
    private static final String HOLDERS_PATH = "/ssoi/cardholders";
    private static final String VIPS_PATH = "/ssoi/personcontrol";
    private static final String HOLDERS_FULL_PATH = ARTIFACT_PATH + HOLDERS_PATH;
    private static final String VIPS_FULL_PATH = ARTIFACT_PATH + VIPS_PATH;
    private static final String GR_HOST = "192.168.0.151"; //"10.28.65.228";
    private static final int GR_PORT = 9443;
    private static final String GR_USER = "Apacs";
    private static final String GR_PASS = "Aa1234567";
    private URL grUrl;
    private Registry registry;
    private List<AdpCardHolder> vipCHs = new ArrayList<>();
    private List<AdpCardHolder> notVipCHs = new ArrayList<>();

    public PcWSOAdapters() {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("carbon.repo.write.mode", "true");
        //System.setProperty("javax.net.ssl.trustStore", GREG_HOME + "/repository/resources/security/client-truststore.jks");
        //System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
        //System.setProperty("javax.net.ssl.trustStoreType","JKS");
        try {
            this.grUrl = new URL("https", GR_HOST, GR_PORT, "/registry");
            this.registry = new RemoteRegistry(grUrl, GR_USER, GR_PASS);            
        } catch (MalformedURLException | RegistryException e) {
            out.print(e.toString());
        }
        ArtifactCacheManager.enableClientCache();
    }

    public boolean fillCHsSets() {
        boolean result;
        try {
            Collection col = (Collection) registry.get(HOLDERS_FULL_PATH);
            for (String resPath : col.getChildren()) {
                Resource res = registry.get(resPath);
                if (res != null && !(res instanceof Collection)) {
                    
                    InputStream contentStream = res.getContentStream();
                    AdpCardHolder ch = new AdpCardHolder(contentStream);
                    String chPath = HOLDERS_FULL_PATH + "/" + ch.getId() + ".xml";
                    String vipPath = VIPS_FULL_PATH + "/" +ch.getId();
                    boolean vipExist = registry.resourceExists(vipPath);
                    if (vipExist != ch.isVip()) {
                        res = setVipValue(res, vipExist);
                        registry.put(chPath, res);
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
        try {
            byte[] resContent = (byte[])res.getContent();
            ByteArrayInputStream bais = new ByteArrayInputStream(resContent);
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setNamespaceAware(true);
            bf.setValidating(false);
            DocumentBuilder builder = bf.newDocumentBuilder();
            Document xdoc = builder.parse(bais);
            Node vip = xdoc.getElementsByTagName("vip").item(0);
            vip.setTextContent(String.valueOf(vipVal));
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource source = new DOMSource(xdoc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            StreamResult sr = new StreamResult(baos);
            transformer.transform(source, sr);
            resContent = baos.toByteArray();
            res.setContent(resContent);
        } catch (RegistryException | TransformerException | ParserConfigurationException | SAXException | IOException | DOMException e) {
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
                    AdpCardHolder ach = new AdpCardHolder(res.getContentStream());
                    if (ach.isVip() != vipValue){
                        res = setVipValue(res, vipValue);
                        registry.put(chPath, res);
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

    public List<AdpCardHolder> getVipCHs() {
        Collections.sort(vipCHs);
        return vipCHs;
    }

    public List<AdpCardHolder> getNotVipCHs() {
        Collections.sort(notVipCHs);
        return notVipCHs;
    }
}
