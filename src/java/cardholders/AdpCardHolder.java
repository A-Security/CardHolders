package cardholders;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class AdpCardHolder implements Comparable<AdpCardHolder>{
    private String id;
    private String name;
    private String shortName;
    private String cardNo;
    private String photoLink;
    private Boolean vip;

    public AdpCardHolder(){
    }
    
    public AdpCardHolder(InputStream is, DocumentBuilder builder) {
        deserializeGRContent(is, builder);
    }

    public AdpCardHolder(String ID, String Name, String ShortName, String CardNo, String PhotoLink, boolean VIP) {
        this.id = ID;
        this.name = Name;
        this.shortName = ShortName;
        this.cardNo = CardNo;
        this.photoLink = PhotoLink;
        this.vip = VIP;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String ShortName) {
        this.shortName = ShortName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String CardNo) {
        this.cardNo = CardNo;
    }

    public String getPhotoLink() {
        return photoLink;
    }

    public void setPhotoLink(String PhotoLink) {
        this.photoLink = PhotoLink;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVIP(String VIP) {
        this.vip = Boolean.valueOf(VIP);
    }
    
    private void deserializeGRContent(InputStream is, DocumentBuilder builder){
        try {
            Document xdoc = builder.parse(is);
            for(Field f : this.getClass().getDeclaredFields()){
                f.setAccessible(true);
                String val = xdoc.getElementsByTagName(f.getName()).item(0).getTextContent();
                f.set(this, (f.getType() != Boolean.class) ? val : Boolean.valueOf(val) );
            }
        } catch (SAXException | IOException | SecurityException | DOMException | IllegalArgumentException | IllegalAccessException ex) {
            System.out.println(ex.toString());
        }
    }

    @Override
    public int compareTo(AdpCardHolder other) {
        return name.compareTo(other.name);
    }
}
