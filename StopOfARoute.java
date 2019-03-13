
/**
 * <p>StopOfARoute sisältää HSL:n yhden reitin yhden pysäkin.</p>
 * 
 * <p>StopOfARoute sisältää myös sen tiedon, millä nimellä tätä pysäkkiä kutsutaan
 * esimerkiksi HSL:n välipisteenä, HelB:in kuljettajanvaihtopisteenä jne.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-03-13
 */

import java.util.HashMap;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;


public class StopOfARoute
{

    /** Yhdeksännumeroinen HSL:n sisäinen pysäkkikoodi. */
    private int stopId;

    /** Matkustajille näkyvä nelinumeroinen pysäkkikoodi, joka saattaa sisältää kuntakoodin
     *  kirjaimin, esimerkiksi "4096" tai "E1234". */
    private String stopCode;

    /** Matkustajille näkyvä pysäkin virallinen nimi. */
    private String stopName;

    /** Välipistenimet. Avaimena on välipistenimiluokka, ja arvona pysäkin välipistenimi. */
    private HashMap<String,String> timingPointNames;
    
    /**
     * Luo uuden objektin. 
     * @param stopId    Yhdeksännumeroinen HSL:n sisäinen pysäkkikoodi.
     * @param stopCode  Matkustajille näkyvä nelinumeroinen pysäkkikoodi.
     * @param stopName  Matkustajille näkyvä pysäkin virallinen nimi.
     */
    public StopOfARoute(int stopId, String stopCode, String stopName) {
        this.stopId = stopId;
        this.stopCode = stopCode;
        this.stopName = stopName;
        timingPointNames = new HashMap<String,String>();
    }
    
    /**
     * <p>Asettaa pysäkin välipistenimen.</p>
     * 
     * <p>Reittien varsilla on mm. HSL:n määrittelemiä välipistepysäkkejä ja HelB:n
     * kuljettajanvaihtopysäkkejä. Näitä kutsutaan eri nimellä kuin mikä on pysäkin virallinen
     * nimi.</p>
     * 
     * <p>Välipistenimet on jaettu eri luokkiin käyttötarkoituksen mukaan, jotta esimerkiksi
     * HelBin kuljettajanvaihtopysäkkien eivät sekoitu Facebook-ajosarjapäivitysten
     * lähtöpysäkkien nimiin.</p>
     * 
     * @param timingPointClass  Välipisteluokka.
     * @param timingPointNime   Välipisteen nimi tässä välipisteluokassa.
     */
    public void setTimingPointName(String timingPointClass, String timingPointName) {
        timingPointNames.put(timingPointClass, timingPointName);
    }
    
    /**
     * <p>Palauttaa pysäkin välipistenimen. Jos välipysäkillä ei ole nimeä valitussa
     * välipisteluokassa, palauttaa tyhjän merkkijonon.</p>
     * 
     * @param timingPointClass  Välipisteluokka.
     * @return                  Välipisteen nimi valitussa välipisteluokassa.
     */
    public String getTimingPointName(String timingPointClass) {
        if (timingPointNames.containsKey(timingPointClass)) {
            return timingPointNames.get(timingPointClass);
        } else {
            return "";
        }
    }
    
    /**
     * <p>Tallentaa pysäkkitiedot XML-tiedostoon.</p>
     * 
     * @param xml   XML-kirjoittajaluokka.
     */
    public void saveXml(XmlSerializer xml) throws java.io.IOException {
        XmlHelper.indent(xml);

        xml.startTag("", "id");
        xml.text(stopId+"");
        xml.endTag("", "id");
        xml.text("\t");

        xml.startTag("", "code");
        xml.text(stopCode+"");
        xml.endTag("", "code");
        xml.text("\t");

        xml.startTag("", "name");
        xml.text(stopName+"");
        xml.endTag("", "name");
        xml.text("\n");
        
        if (!timingPointNames.isEmpty()) {
            for (String key : timingPointNames.keySet()) {
                XmlHelper.indent(xml);
                xml.startTag("", "timing_point");
                xml.attribute("", "class", key);
                xml.text(timingPointNames.get(key));
                xml.endTag("", "timing_point");
                xml.text("\n");
            }
        }
    }
    
    /**
     * <p>Lukee pysäkkitiedot XML-tiedostosta.</p>
     * 
     * @param xml   XML-kirjoittajaluokka.
     */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("stop")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("id")) {
                xml.xpp.next();
                this.stopId = Integer.parseInt(xml.xpp.getText());
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("code")) {
                xml.xpp.next();
                this.stopCode = xml.xpp.getText();
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("name")) {
                xml.xpp.next();
                this.stopName = xml.xpp.getText();
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("timing_point")) {
                String key = xml.xpp.getAttributeValue(0);
                xml.xpp.next();
                String value = xml.xpp.getText();
                timingPointNames.put(key, value);
                xml.xpp.next();
            } else {
                System.out.println("Unknown tag in a stop! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }
    
    /** @return Yhdeksännumeroinen HSL:n sisäinen pysäkkikoodi. */
    public int getStopId() { 
        return stopId; 
    }

    /** @param id Yhdeksännumeroinen HSL:n sisäinen pysäkkikoodi. */
    public void setStopId(int id) { 
        stopId = id; 
    }
    
    /** 
     * Palauttaa matkustajille näkyvän nelinumeroisen pysäkkikoodin, joka saattaa sisältää 
     * kuntakoodin kirjaimin, esimerkiksi "4096" tai "E1234". 
     *  
     * @return Nelinumeroinen pysäkkikoodi.
     */
    public String getStopCode() { 
        return stopCode; 
    }
    
    /** 
     * Asettaa matkustajille näkyvän nelinumeroisen pysäkkikoodin, joka saattaa sisältää 
     * kuntakoodin kirjaimin, esimerkiksi "4096" tai "E1234". 
     *  
     * @param code Nelinumeroinen pysäkkikoodi.
     */
    public void setStopCode(String code) { 
        stopCode = code; 
    }
    
    /** @return Matkustajille näkyvä pysäkin virallinen nimi. */
    public String getStopName() { 
        return stopName; 
    }
    
    /** @param name Matkustajille näkyvä pysäkin virallinen nimi. */
    public void setStopName(String name) { 
        stopName = name; 
    }

}
