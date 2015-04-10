package personcontrol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static java.lang.System.out;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.wso2.carbon.registry.app.RemoteRegistry;
import org.datacontract.schemas._2004._07.apacsadapter.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
        try {
            grUrl = new URL("https", GR_HOST, GR_PORT, "/registry");
            registry = new RemoteRegistry(grUrl, GR_USER, GR_PASS);
        } catch (MalformedURLException | RegistryException e) {
            out.print(e.toString());
        }
    }

    public boolean fillGRCardHolders() {
        AdpWebService service = new AdpWebService();
        AdpWebServiceSoap port = service.getAdpWebServiceSoap();
        return port.fillGRCardHolders();
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
                    if (ch.isVip()){
                        if (registry.resourceExists(vippath)){
                            vipCHs.add(ch);
                        }
                        else {
                            contentStream = setVipValue(contentStream, false);
                            res.setContentStream(contentStream);
                            registry.put(resPath, res);
                        }
                    }
                    else {
                        if (registry.resourceExists(vippath)){
                            contentStream = setVipValue(contentStream, true);
                            res.setContentStream(contentStream);
                            registry.put(resPath, res);
                        }
                        else {
                            notVipCHs.add(ch);
                        }
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

    public boolean setCHsVIP(String[] vips) {
        boolean result = false;
        if (vips == null){
            return result;
        }
        try {
            for (String vip : vips) {
                String chPath = HOLDERS_FULL_PATH + "/" + vip + ".xml";
                String vipPath = VIPS_FULL_PATH + "/" + vip;
                if (!registry.resourceExists(vipPath)) {
                    registry.put(vipPath, registry.newResource());
                }
                if (registry.resourceExists(chPath)) {
                    Resource res = registry.get(chPath);
                    InputStream contentStream = res.getContentStream();
                    contentStream = setVipValue(contentStream, true);
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
    
    public boolean remCHsVIP(String[] notvips) {
        boolean result = false;
        if (notvips == null){
            return result;
        }
        try {
            for (String notvip : notvips) {
                String chPath = HOLDERS_FULL_PATH + "/" + notvip + ".xml";
                String vipPath = VIPS_FULL_PATH + "/" + notvip;
                if (registry.resourceExists(vipPath)) {
                    registry.delete(vipPath);
                }
                if (registry.resourceExists(chPath)) {
                    Resource res = registry.get(chPath);
                    InputStream contentStream = res.getContentStream();
                    contentStream = setVipValue(contentStream, false);
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
