package personcontrol;

import java.io.*;
import static java.lang.System.out;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.*;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.dom4j.io.*;
import org.w3c.dom.*;
import org.wso2.carbon.governance.api.cache.ArtifactCacheManager;
import org.wso2.carbon.registry.core.*;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.xml.sax.SAXException;

public class PcWSOAdapters {

    private static final String ARTIFACT_PATH = "/_system/governance";
    private static final String HOLDERS_PATH = "/ssoi/cardholders";
    private static final String VIPS_PATH = "/ssoi/personcontrol";
    private static final String HOLDERS_FULL_PATH = ARTIFACT_PATH + HOLDERS_PATH;
    private static final String VIPS_FULL_PATH = ARTIFACT_PATH + VIPS_PATH;
    private static final String GR_HOST = "192.168.0.151"; //"10.28.65.228";
    private static final int GR_PORT = 9443;
    private static final String GR_USER = "admin";
    private static final String GR_PASS = "Flvbybcnhfnjh1";
    private URL grUrl;
    private Registry registry;
    private List<AdpCardHolder> vipCHs = new ArrayList<>();
    private List<AdpCardHolder> notVipCHs = new ArrayList<>();

    public PcWSOAdapters() {
        System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
        System.setProperty("carbon.repo.write.mode", "true");
        try {
            grUrl = new URL("https", GR_HOST, GR_PORT, "/registry");
            registry = new RemoteRegistry(grUrl, GR_USER, GR_PASS);
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
                    String vippath = VIPS_FULL_PATH + "/" + ch.getId();
                    if (registry.resourceExists(vippath)){
                        if (!ch.isVip()){
                            contentStream = setVipValue(contentStream, true);
                            res.setContentStream(contentStream);
                            registry.put(resPath, res);
                        }
                        vipCHs.add(ch);
                    }
                    else {
                        if (ch.isVip()){
                            contentStream = setVipValue(contentStream, false);
                            res.setContentStream(contentStream);
                            registry.put(resPath, res);
                        }
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
                    InputStream contentStream = res.getContentStream();
                    contentStream = setVipValue(contentStream, vipValue);
                    res.setContentStream(contentStream);
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

    public List<AdpCardHolder> getVipCHs() {
        Collections.sort(vipCHs);
        return vipCHs;
    }

    public List<AdpCardHolder> getNotVipCHs() {
        Collections.sort(notVipCHs);
        return notVipCHs;
    }

    private InputStream setVipValue(InputStream contentStream, boolean vipVal) {
        try {
            DocumentBuilderFactory bf = DocumentBuilderFactory.newInstance();
            bf.setValidating(false);
            DocumentBuilder builder = bf.newDocumentBuilder();
            Document xdoc = builder.parse(contentStream);
            Node vip = xdoc.getElementsByTagName("vip").item(0);
            vip.setTextContent(String.valueOf(vipVal));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLWriter xmlWriter = new XMLWriter(outputStream, OutputFormat.createPrettyPrint());
            xmlWriter.write(xdoc);
            xmlWriter.close();
            contentStream = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (ParserConfigurationException | SAXException | IOException | DOMException e) {
            out.print(e.toString());
            contentStream = null;
        }
        return contentStream;
    }

}
