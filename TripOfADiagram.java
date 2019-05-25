package helbtrafficdata;

import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * Sisältää yhden viittauksen HSL:n aikatauludatan linjasivuun.
 * @author Tuomas Lehti
 * @version 2019-05-25
*/
public class TripOfADiagram 
{

	/** Linjasivun HSL-linjatunnus. */
    private String routeId;
    
    /** Linjasivun suunta. (1/2) */
    private String direction;
    
    /** Linjasivun lähtöaika. */
    private short departureTime;

	/** @return Linjasivun HSL-linjatunnus merkkijonona. */
    public String getRouteId() {
        return routeId;
    }
    
    /** @return Linjasivun suunta. (1/2) */
    public String getDirection() {
        return direction;
    }
    
	/** @return Linjasivun HSL-linjatunnus. */
    public RouteIdAndDir getRouteIdAndDir() {
        return new RouteIdAndDir(getRouteId(), getDirection());
    }
    
    /** @return Linjasivun lähtöaika. */
    public short getDepartureTime() {
        return departureTime;
    }
    
    /**
     * Asettaa linjatunnuksen.
     * @param id Reittitunnus merkkijonona.
     */
    public void setRouteId(String id) {
        routeId = id;
    }

    /**
     * Asettaa suunnan.
     * @param dir Suunta merkkijonona.
     */
    public void setDirection(String dir) {
        direction = dir;
    }

    /**
     * Asettaa lähtöajan.
     * @param time Lähtöaika muodossa hhmm.
     */
    public void setDepartureTime(short time) {
        departureTime = time;
    }
    
    /** Tallentaa linjasivun xml-tiedostoon. */
    public void saveXml(XmlHelper xml) throws java.io.IOException, XmlPullParserException
    {
        xml.indent();
        xml.serializer.startTag("", "trip");
        xml.serializer.text("\t");

        xml.serializer.startTag("", "route_id");
        xml.serializer.text(this.routeId);
        xml.serializer.endTag("", "route_id");
        xml.serializer.text("\t");
        
        xml.serializer.startTag("", "dir");
        xml.serializer.text(this.direction);
        xml.serializer.endTag("", "dir");
        xml.serializer.text("\t");

        xml.serializer.startTag("", "dep_time");
        xml.serializer.text(Integer.toString(this.departureTime));
        xml.serializer.endTag("", "dep_time");
        xml.serializer.text("\t");
        
        xml.serializer.endTag("", "trip");
        xml.newline();

    }
    
    /** Lataa linjasivun xml-tiedostosta. */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException
    {
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("trip")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("route_id")) {
                xml.xpp.next();
                this.routeId = xml.xpp.getText();
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("dir")) {
                xml.xpp.next();
                this.direction = xml.xpp.getText();
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("dep_time")) {
                xml.xpp.next();
                this.departureTime = (short) Integer.parseInt(xml.xpp.getText());
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("duty_change")) {
            } else {
                System.out.println("Unknown tag in a trip of a diagram! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }

    
}
