package helbtrafficdata;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/** 
 * Sisältää Helsingin bussiliikenteen vuoroaikataulut. 
 * @author Tuomas Lehti
 * @version 2019-05-25
 */
public class HelbTrafficData
{

    /** HSL:n aikatauludata. */
    RouteArray timetables = new RouteArray();
    
    /** Vuoroaikataulut. */
    Map<DayType,DiagramArray> diagrams = new TreeMap<DayType,DiagramArray>();
   
    public RouteArray getTimetables() {
        return timetables;
    }

    public void setTimetables(RouteArray ttbls) {
    	timetables = ttbls;
    }

    /** 
     * Palauttaa tietyn päivätyypin vuoroaikataululistan.
     * @param dayType Haluttu päivätyyppi.
     * @return Vuoroaikataululista, tai null, jos halutulle päivätyypille ei
     *         ole vuoroaikatauluja.
     */
    public DiagramArray getDiagrams(DayType dayType) {
        if (diagrams.containsKey(dayType)) {
            return diagrams.get(dayType);
        } else {
            return null;
        }
    }

    /** Palauttaa tiedon, onko tietyllä päivätyypillä vuoroaikatauluja. */
    public boolean hasDiagrams(DayType dayType) {
        return getDiagrams(dayType) != null;
    }
    
    public void addDiagram(DayType dayType, Diagram diagram) {
        if (!hasDiagrams(dayType)) {
            diagrams.put(dayType, new DiagramArray(dayType));
        }
        diagrams.get(dayType).addDiagram(diagram);
    }
    
    public void saveXml(String filename) {
        XmlSerializer xpp = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            xpp = XmlPullParserFactory
                    .newInstance(System.getProperty(XmlPullParserFactory.PROPERTY_NAME), null)
                    .newSerializer();
            xpp.setOutput(writer);
            
            XmlHelper xml = new XmlHelper(xpp);
            
            xpp.startTag("", "HelB");
            xpp.attribute("", "start_date", "2019-01-07");
            xml.newline();
            timetables.saveXml(xpp);
            
            if (diagrams.size() > 0) {
                
                Set<DayType> keys = diagrams.keySet();
                for (DayType key : keys) {
                    xml.indent();
                    xml.serializer.startTag("", "diagrams");
                    diagrams.get(key).saveXml(xml);
                    xml.iEndTagLn("diagrams");
                }

            }
         
            xpp.endTag("", "HelB");
            writer.close();
        } catch (Exception e) {
            System.out.println("exception");
            System.out.println(e);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                System.out.println("Something's very wrong. I can't even close the file.");
            }
        }
        System.out.println("Done...");
        
    }
    
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException
    {
        xml.xpp.next(); // jump to HelB
        xml.xpp.next(); // jump to whitespace
        while (!xml.atEndOfDocument() && !xml.atEndTag("HelB")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("routes")) {
                xml.xpp.next();
                timetables.loadXml(xml);
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("diagrams")) {
                //xml.xpp.next();
                DiagramArray diagramArray = new DiagramArray(null);
                diagramArray.loadXml(xml);
                diagrams.put(diagramArray.getDayType(), diagramArray);
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
    
    public void loadXml(String filename) throws java.io.IOException/*, XmlPullParserException*/
    {
        XmlPullParser xpp = null;
     
        try {
            System.out.println("Avataan tiedostoa...");
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            xpp.setInput(reader);
        } catch (Exception e) {
            System.out.println(e);
        }

        XmlPullHelper xml = new XmlPullHelper(xpp);
        
        try {
            System.out.println("Luetaan aikatauluja...");
            loadXml(xml);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
    
    
}
