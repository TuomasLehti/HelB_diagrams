package helbtrafficdata;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

/**
 * <p>Route on HSL:n yhden reitin sisältävä luokka.</p>
 * 
 * <p>Jokaisen linjan eri suunnat on jaettu eri reiteiksi. Reitti sisältää sen 
 * pysäkit, joihin pääsee käsiksi getStop- ja addStop-metodeilla ja aikataulut.</p>
 * 
 * <p>Aikatauluihin pääsee käsiksi setTime-, getTime- ja searchTrip-metodeilla.
 * Näissä metodeissa ajat tallennetaan kuin helbtrafficdata-luokassa muutenkin,
 * eli siten, että luvun ykköset ja kymmenet ilmoittavat minuuttiluvun, ja sadat 
 * ja tarvittaessa tuhannet tunnin. Esimerkiksi puolipäivä merkitään 1200 ja 
 * varttia yli yhdeksän 945.</p>
 *
 * <p>Tietojen asettamiseen ja hakuun liittyvät metodit ovat saatavana kahtena
 * eri versiona. Voidaan käyttää päivätyypin merkitsemiseen joko DayType-luokkaa
 * tai vanhan tyylin mukaisesti kokonaislukua, eli 0 = ma, 1 = pe, 2 = la, 
 * 3 = su. Tähän tullee muutos jatkossa.</p>
 *
 * <p>Sisäisesti päivätyyppi käsitellään vielä kokonaislukuna. Tämä muuttunee
 * seuraavan, suuremman refaktoroinnin yhteydessä.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-09-29
 */
public class Route //implements Comparable<Route>
{
	/** Reitin HSL-tunnus (esim. 1093K) ja suunta (1 tai 2). */
    private RouteIdAndDir idAndDir = null;
    
    /** Lista tämän reitin pysäkeistä. */
    private ArrayList<StopOfARoute> stops = new ArrayList<StopOfARoute>();
    
    /** Tämän reitin linjasivut. */
    private ArrayList<TripTable> timetables = new ArrayList<TripTable>();
    
    private final static String DAY_TYPES = "MaPeLaSu";

    /**
     * Luo luokasta instanssin.
     * @param idAndDir Reitin HSL-tunnus.
     */
    public Route(RouteIdAndDir idAndDir){
        this.idAndDir = idAndDir;
        timetables.add(new TripTable());
        timetables.add(new TripTable());
        timetables.add(new TripTable());
        timetables.add(new TripTable());
    }
    
    /**
     * Luo luokasta instanssin.
     * @param idAndDir Reitin HSL-tunnus.
     */
    public Route() {
        timetables.add(new TripTable());
        timetables.add(new TripTable());
        timetables.add(new TripTable());
        timetables.add(new TripTable());
    }

    /**
     * @return Reitin HSL-tunnus.
     */
    public RouteIdAndDir getIdAndDir() {
        return this.idAndDir;
    }
    
    /**
     * Asettaa ajan aikataulutaulukkoon.
     * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
     * @param tripIdx Linjasivun järjestysnumero 0 -> n-1.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @param time Asetettava aika, tunnit satoina, minuutit ykkösinä.
     */
    public void setTime(int dayType, int tripIdx, int stopIdx, short time) {
        TripTable timetable = timetables.get(dayType);
        timetable.setTime(tripIdx, stopIdx, time);
    }

    /**
     * Asettaa ajan aikataulutaulukkoon.
     * @param dayType Päivätyyppi.
     * @param tripIdx Linjasivun järjestysnumero 0 -> n-1.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @param time Asetettava aika, tunnit satoina, minuutit ykkösinä.
     */
    public void setTime(DayType dayType, int tripIdx, int stopIdx, short time) {
        TripTable timetable = timetables.get(dayType.getI());
        timetable.setTime(tripIdx, stopIdx, time);
    }

    /**
     * Lukee ajan aikataulutaulukosta.
     * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
     * @param tripIdx Linjasivun järjestysnumero 0 -> n-1.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @return Haluttu aika.
     */
    public short getTime(int dayType, int tripIdx, int stopIdx) {
        TripTable timetable = timetables.get(dayType);
        return timetable.getTime(tripIdx, stopIdx);
    }

    /**
     * Lukee ajan aikataulutaulukosta.
     * @param dayType Päivätyyppi.
     * @param tripIdx Linjasivun järjestysnumero 0 -> n-1.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @return Haluttu aika.
     */
    public short getTime(DayType dayType, int tripIdx, int stopIdx) {
        TripTable timetable = timetables.get(dayType.getI());
        return timetable.getTime(tripIdx, stopIdx);
    }
    
    /**
     * <p>Etsii ajan aikataulutaulukosta.</p>
     * <p>Etsii aikaa tietyn pysäkin kohdalta. Palauttaa sen linjasivun
     * järjestysnumeron, jolla haluttu aika esiintyy ensimmäisen kerran.</p>
     * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @param time Etsittävä aika, tunnit satoina, minuutit ykkösinä.
     * @return Linjasivun järjestysnumero.
     */
    public int searchTrip(int dayType, int stopIdx, short time) {
        TripTable timetable = timetables.get(dayType);
        return timetable.searchTrip(stopIdx, time);
    }

    /**
     * <p>Etsii ajan aikataulutaulukosta.</p>
     * <p>Etsii aikaa tietyn pysäkin kohdalta. Palauttaa sen linjasivun
     * järjestysnumeron, jolla haluttu aika esiintyy ensimmäisen kerran.</p>
     * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
     * @param stopIdx Pysäkin järjestysnumero 0 -> n-1.
     * @param time Etsittävä aika, tunnit satoina, minuutit ykkösinä.
     * @return Linjasivun järjestysnumero.
     */
    public int searchTrip(DayType dayType, int stopIdx, short time) {
        TripTable timetable = timetables.get(dayType.getI());
        return timetable.searchTrip(stopIdx, time);
    }
    
    /** 
    * <p>Lajittelee tietyn päivän lähdöt ensimmäisen pysäkin lähtöajan mukaan.</p>
    * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
    */
    public void sortTrips(int dayType) {
    	timetables.get(dayType).sortTrips();
    }

    /** 
    * <p>Lajittelee tietyn päivän lähdöt ensimmäisen pysäkin lähtöajan mukaan.</p>
    * @param dayType Päivätyyppi. 0 = ma, 1 = pe, 2 = la, 3 = su.
    */
    public void sortTrips(DayType dayType) {
    	timetables.get(dayType.getI()).sortTrips();
    }
    
    /**
     * Lisää pysäkin pysäkkilistaan.
     * @param stop Lisättävä pysäkki.
     */
    public void addStop(StopOfARoute stop) {
        stops.add(stop);
    }
    
    /**
     * Palauttaa pysäkin pysäkkilistalta.
     * @return Pysäkin objekti.
     */
    public StopOfARoute getStop(int stopIndex) {
        return stops.get(stopIndex);
    }
    
    /**
     * Etsii pysäkin tietyllä pysäkkikoodilla. Jos pysäkkiä ei löydy, 
     * palauttaa -1.
     * @param stopId Pysäkin HSL:n sisäinen yhdeksännumeroinen koodi.
     * @return Pysäkin järjestysnumero.
     */
    public int getStopIdxWithId(int stopId) {
        int idx = 0;
        while (idx < getNumOfStops() && getStop(idx).getStopId() != stopId) {
            idx++;
        }
        if (idx >= getNumOfStops()) {
            idx = -1;
        }
        return idx;
    }
    
    /** 
     * Palauttaa tietyn päivätyypin linjasivujen määrän.
     * @param dayType Päivätyyppi.
     * @return Linjasivujen määrä.
     */ 
    public int getNumOfTrips(int dayType) {
        return timetables.get(dayType).getNumOfTrips();
    }

    /** 
     * Palauttaa tietyn päivätyypin linjasivujen määrän.
     * @param dayType Päivätyyppi.
     * @return Linjasivujen määrä.
     */ 
    public int getNumOfTrips(DayType dayType) {
        return timetables.get(dayType.getI()).getNumOfTrips();
    }
    
    /** 
     * Palauttaa tämän reitin pysäkkien määrän.
     * @return Pysäkkien määrä.
     */ 
    public int getNumOfStops() {
        return stops.size();
    }

    /** 
    * Tallentaa reitin XML-tiedostoon.
    */
    public void saveXml(XmlSerializer xml) throws java.io.IOException {
        XmlHelper.indent(xml);
        xml.startTag("", "route");
        xml.attribute("", "id", this.idAndDir.getId());
        xml.attribute("", "dir", this.idAndDir.getDir());
        XmlHelper.newline(xml);
        saveStops(xml);
        saveTrips(xml);
        XmlHelper.indentEndTagNewLine(xml, "route");
    }

   /** Tallentaa pysäkit XML-tiedostoon. */
   private void saveStops(XmlSerializer xml) throws java.io.IOException {
        XmlHelper.indent(xml);
        xml.startTag("", "stops");
        XmlHelper.newline(xml);
        for (StopOfARoute stop : stops) {
            XmlHelper.indent(xml);
            xml.startTag("", "stop");
            XmlHelper.newline(xml);
            stop.saveXml(xml);
            XmlHelper.indentEndTagNewLine(xml, "stop");
        }
        XmlHelper.indentEndTagNewLine(xml, "stops");
    }
    
   /** Tallentaa kunkin päivätyypin aikataulutaulukon XML-tiedostoon. */
    private void saveTrips(XmlSerializer xml)  throws java.io.IOException {
        for (int i = 0; i < 4; i++) {
            XmlHelper.indent(xml);
            xml.startTag("", "trips");
            xml.attribute("", "day_type", DAY_TYPES.substring(i*2, i*2+2));
            XmlHelper.newline(xml);
            timetables.get(i).saveXml(xml);
            XmlHelper.indentEndTagNewLine(xml, "trips");
        }
    }
    
   /** 
    * Lukee reitin XML-tiedostosta.
    */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        
        // Find the start of the route.
        while (!xml.atEndOfDocument() && !xml.atStartTag("route")) {
            xml.xpp.next();
        }

        loadIdAndDir(xml);
        
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("route")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("stops")) {
                loadStops(xml); 
            } else if (xml.xpp.getName().equals("trips")) {
                loadTrips(xml);
            } else {
                System.out.println("Unknown tag in route!");
            }
            // Load*-methods should leave the parser to their own end tag.
            // We can then find the next tag.
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
        
        
    }
    
    private void loadIdAndDir(XmlPullHelper xml) throws XmlPullParserException {
        if (xml.xpp.getAttributeCount() == 2) {
            String id = "";
            String dir = "";
            for (int i = 0; i < 2; i++) {
                String attrName = xml.xpp.getAttributeName(i);
                String attrValue = xml.xpp.getAttributeValue(i);
                if (attrName.equals("id")) {
                    id = attrValue;
                } else if (attrName.equals("dir")) {
                    dir = attrValue;
                } else {
                    System.out.println("Unknown attribute!");
                }
            }
            this.idAndDir = new RouteIdAndDir(id, dir);
        } else {
            System.out.println("Wrong number of attributes!");
        }
    }
    
    private void loadStops(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("stops")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("stop")) {
                StopOfARoute stop = new StopOfARoute(0,"","");
                stop.loadXml(xml);
                stops.add(stop);
            } else {
                System.out.println("Unknown tag in stops!");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }

    private void loadTrips(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        String dayType = xml.xpp.getAttributeValue(0);
        int dayTypeIndex = DAY_TYPES.indexOf(dayType) / 2;
        xml.xpp.next();
        timetables.get(dayTypeIndex).loadXml(xml);
        while (!xml.atEndOfDocument() && !xml.atATag()) {                
            xml.xpp.next();
        }
    }
    
}

/**
* TODO:
* -- DAY_TYPES pois
* -- Luokka käyttämään DayType-luokan palveluita.
*
* muutokset:
* 2019-06-16: lisätty sortTrips.
* 2019-09-28: Lisätty metodit, jotka käyttävät DayType-luokan palveluita.
*/
