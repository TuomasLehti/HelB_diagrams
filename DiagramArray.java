package helbtrafficdata;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Set;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * DiagramArray sis‰lt‰‰ Helsingin bussiliikenteen vuoroaikataulut, siis kunkin 
 * bussin tyˆp‰iv‰t.
 * @author Tuomas Lehti
 * @version 2019-05-25
 */
public class DiagramArray
{
	/** XML-tiedoston tagi, joka sis‰lt‰‰ vuoroaikataulut. */
    private static String TAG_DIAGRAMS = "diagrams";
    
    /** XML-tiedoston attribuutti, joka sis‰lt‰‰ p‰iv‰tyypin. */
    private static String ATTR_DAYTYPE = "day_type";
    
    /** T‰m‰n objektin sis‰lt‰mien vuoroaikataulujen p‰iv‰tyyppi. */
    private DayType dayType = null;

    /** 
     * Vuoroaikataultu sis‰lt‰v‰ Map. Avaimena on vuoron numero kokonais-
     * lukuna ja arvona itse vuoroaikataulu.
     */
    private Map<Integer,Diagram> diagrams = new TreeMap<Integer,Diagram>();
    
    /**
     * Luo luokasta instanssin.
     * @param dt Vuoroaikataulujen p‰iv‰tyyppi.
     */
    public DiagramArray(DayType dt) {
        dayType = dt;
    }
    
    /**
     * Palauttaa vuoroaikataulun.
     * @param diagramId Halutun vuoroaikataulun numero.
     */
    public Diagram getDiagram(Integer diagramId) {
        return diagrams.get(diagramId);
    }
    
    /**
     * Lis‰‰ vuoroaikataulun.
     * @param diagram Lis‰tt‰v‰ vuoroaikataulu.
     */
    public void addDiagram(Diagram diagram) {
        diagrams.put(diagram.getDiagramId(), diagram);
    }
    
    /**
     * Poistaa vuoroaikataulun.
     * @param diagramId Poistettavan vuoroaikataulun numero.
     */
    public void removeDiagram(Integer diagramId) {
        diagrams.remove(diagramId);
    }
    
    /**
     * @return T‰m‰n vuoroaikataululistan p‰iv‰tyyppi.
     */
    public DayType getDayType() {
        return dayType;
    }
    
    /**
     * @return T‰m‰n vuoroaikataululistan sis‰lt‰mien vuoroaikataulujen numerot.
     */
    public Set<Integer> getDiagramIds() {
        return diagrams.keySet();
    }

    /** Tallentaa vuoroaikataululistan xml-tiedostoon. */
    public void saveXml(XmlHelper xml) throws java.io.IOException, XmlPullParserException
    {
        xml.serializer.attribute("", ATTR_DAYTYPE, dayType.getS());
        xml.newline();
        for (Integer key : diagrams.keySet()) {
            diagrams.get(key).saveXml(xml);
        }
    }

    /** Lataa vuoroaikataululistan xml-tiedostosta. */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException
    {

        dayType = new DayType(xml.xpp.getAttributeValue(0));
//        dayType.setS(xml.xpp.getAttributeValue(0));
//        dayType = new DayType(xml.xpp.getAttributeValue(0));
//        System.out.println("Reading diagrams for day type "+dayType);
        
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag(TAG_DIAGRAMS)) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("diagram")) {
//                xml.xpp.next();
                Diagram diagram = new Diagram();
                diagram.loadXml(xml);
                diagrams.put(diagram.getDiagramId(), diagram);
                xml.xpp.next();
            } else {
                System.out.println("Unknown tag in a diagrams! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }

}
