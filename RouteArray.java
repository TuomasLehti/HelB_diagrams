package helbtrafficdata;

import java.util.Set;
import java.util.TreeMap;
import java.util.HashMap;
import java.util.Scanner;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/**
 * <p>RouteArray sisältää HSL:n aikatauludataa.</p>
 * 
 * @author Tuomas Lehti
 * @version 2019-05-23
 */

public class RouteArray
{

	/** Kaikki aikataulut. */
    private TreeMap<RouteIdAndDir,Route> routes = new TreeMap<RouteIdAndDir,Route>();
    
    /**
     * @param idAndDir Palautettavan reitin tunnus, muodossa 1093Ks1.
     * @return Haluttu reitti.
    */
    public Route getRoute(String idAndDir) {
        return getRoute(new RouteIdAndDir(idAndDir));
    }
    
    /**
     * @param idAndDir Palautettavan reitin tunnus.
     * @return Haluttu reitti.
    */
    public Route getRoute(RouteIdAndDir idAndDir) {
        if (routes.containsKey(idAndDir)) {
            return routes.get(idAndDir);
        } else {
            return null;
        }
    }
    
    /**
     * @param idAndDir Lisättävän reitin tunnus, muodossa 1093Ks1.
     * @param route Lisättävä reitti.
    */
    public void addRoute(String idAndDir, Route route) {
        routes.put(new RouteIdAndDir(idAndDir), route);
    }
    
    /**
     * @param idAndDir Lisättävän reitin tunnus.
     * @param route Lisättävä reitti.
    */
    public void addRoute(RouteIdAndDir idAndDir, Route route) {
        routes.put(idAndDir, route);
    }
    
    /**
     * Lisää reitin listaan. Reitin tunnus haetaan reitin sisältä.
     * @param route Lisättävä reitti.
    */
    public void addRoute(Route route) {
        routes.put(route.getIdAndDir(), route);
    }

    /**
     * Kertoo, sisältääkö reittilista tietyn reitin.
     * @return Tieto, onko reitti listassa. 
     */
    public boolean hasRoute(String idAndDir) {
        return routes.containsKey(new RouteIdAndDir(idAndDir));
    }
    
    /**
     * Palauttaa kaikkien listassa olevien reittien tunnusten joukon.
     * @return Tunnusten joukko.
     */
    public Set<RouteIdAndDir> getRouteIdsAndDirs() {
        return routes.keySet();
    }
    
    /** Tallentaa reittilistan xml-tiedostoon. */
    public void saveXml(XmlSerializer xml) {
        try {
            XmlHelper.indentStartTagNewLine(xml, "routes");
            for (RouteIdAndDir id : getRouteIdsAndDirs()) {
                getRoute(id).saveXml(xml);
            }
            XmlHelper.indentEndTagNewLine(xml, "routes");
        } catch (Exception e) {
            System.out.println("exception");
        }
    }
    
    /** Lataa reittilistan xml-tiedostosta. */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("routes")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("route")) {
                Route route = new Route();
                route.loadXml(xml);
                addRoute(route);
            } else {
                System.out.println("Unknown tag in routes! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
        
        
    }
    

}
