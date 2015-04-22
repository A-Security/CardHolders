/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cardholders;

/**
 *
 * @author vudalov
 */
public class ApcFaultEvents {
    private String holderid;
    private String eventid;
    private String holdername;
    private String eventtypedesc;
    private String sourcename;
    private String eventtime;
    
    public ApcFaultEvents() {
    }
    
    public ApcFaultEvents(Object[] objV) {
        if (objV == null || objV.length != 6)
        {
            return;
        }
        holderid = (String)objV[0];
        eventid = (String)objV[1];
        holdername = (String)objV[2];
        eventtypedesc = (String)objV[3];
        sourcename = (String) objV[4];
        eventtime = (String)objV[5];
    }
    
    /**
     * @return the holderid
     */
    public String getHolderid() {
        return holderid;
    }

    /**
     * @param holderid the holderid to set
     */
    public void setHolderid(String holderid) {
        this.holderid = holderid;
    }

    /**
     * @return the eventid
     */
    public String getEventid() {
        return eventid;
    }

    /**
     * @param eventid the eventid to set
     */
    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    /**
     * @return the holdername
     */
    public String getHoldername() {
        return holdername;
    }

    /**
     * @param holdername the holdername to set
     */
    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }

    /**
     * @return the eventtypedesc
     */
    public String getEventtypedesc() {
        return eventtypedesc;
    }

    /**
     * @param eventtypedesc the eventtypedesc to set
     */
    public void setEventtypedesc(String eventtypedesc) {
        this.eventtypedesc = eventtypedesc;
    }

    /**
     * @return the sourcename
     */
    public String getSourcename() {
        return sourcename;
    }

    /**
     * @param sourcename the sourcename to set
     */
    public void setSourcename(String sourcename) {
        this.sourcename = sourcename;
    }

    /**
     * @return the eventtime
     */
    public String getEventtime() {
        return eventtime;
    }

    /**
     * @param eventtime the eventtime to set
     */
    public void setEventtime(String eventtime) {
        this.eventtime = eventtime;
    }
}
