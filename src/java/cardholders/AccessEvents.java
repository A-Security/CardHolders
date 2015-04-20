package cardholders;

import static java.lang.System.out;
import java.util.Arrays;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author vudalov
 */
public class AccessEvents {

    Session session = null;
    public AccessEvents(){
        try {
            this.session = HibernateUtil.getSessionFactory().getCurrentSession();
        }
        catch (Exception e) {
            out.print(Arrays.toString(e.getStackTrace()));
        }
    }
    
    public List<ApacseventsCha> getAccessFaults () {
        List<ApacseventsCha> aeCha = null;
        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery ("SELECT AEC.holderid, AEC.eventid, AEC.holdername, AEC.eventtypedesc, AEC.sourcename, AEC.eventtime\n" +
                                            "FROM ApacseventsCha AEC\n" +
                                            "WHERE AEC.to_date(eventtime, 'YYYY-MM-DD\"T\"HH24:MI:SS.MS') = CURRENT_DATE\n" +
                                                "AND AEC.eventtype NOT LIKE 'TApcCardHolderAccess_Granted'\n" +
                                            "ORDER BY holdername, eventid");
            aeCha = (List<ApacseventsCha>) q.list();
        } catch (Exception e) {
            out.print(Arrays.toString(e.getStackTrace()));
        }
        return aeCha;
    }
}
