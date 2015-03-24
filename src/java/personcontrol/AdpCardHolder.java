package personcontrol;

public class AdpCardHolder {
    private String ID;
    private String Name;
    private String ShortName;
    private String CardNo;
    private byte[] Photo;
    
    public String getID(){ return ID; }
    public void setID(String ID){ this.ID = ID; }
    public String getName() { return Name; }
    public void setName(String Name) { this.Name = Name; }
    public String getShortName() { return ShortName; }
    public void setShortName(String ShortName) { this.ShortName = ShortName; }
    public String getCardNo() { return CardNo; }
    public void setCardNo(String CardNo) { this.CardNo = CardNo; }
    public byte[] getPhoto() { return Photo; }
    public void setPhoto(byte[] Photo) { this.Photo = Photo; }
}
