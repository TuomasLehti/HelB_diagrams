package helbtrafficdata;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * <p>TripTable on varsinaista aikataulutaulukkoa säilyttävä luokka.</p>
 *
 * <p>Ajat säilötään kaksiulotteiseen taulukkoon, jonka pystyriveissä eli 
 * sarakkeissa ovat lähdöt ja vaakariveissä eli riveissä ovat pysäkit.</p>
 *
 * <p>Ajat tallennetaan kokonaislukuina, jonka muoto on kompromissi pienen
 * muistijäljen ja ihmisluettavuuden kannalta. Luvun ykköset ja kymmenet
 * ilmoittavat minuuttiluvun, ja sadat ja tarvittaessa tuhannet tunnin.
 * Esimerkiksi puolipäivä merkitään 1200 ja varttia yli yhdeksän 945.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-05-12
 */
public class TripTable
{
    /** The initial number of trips. */
    private final static int INITIAL_TRIP_CAPACITY = 8;
    
    /** The initial number of times per trip. Each stop has a time. */
    private final static int INITIAL_STOP_CAPACITY = 16;
    
    /** The actual timetable is a two-dimensional array of times expressed as
     *  shorts, in form hhmm. */
    private short[][] timetable;
    
    /** The actual number of trips. There may be empty memory allocations beyond tripSize. */
    private int numOfTrips = 0;
    
    /** The actual number of stops. There may be empty memory allocations beyond tripSize. */
    private int numOfStops = 0;
    
    /** The actual capacity of trips. */
    private int tripCapacity = INITIAL_TRIP_CAPACITY;
    
    /** The actual capacity of stops. */
    private int stopCapacity = INITIAL_STOP_CAPACITY;
    
    /** Creates a new table with initial capacity. */
    public TripTable() {
        timetable = new short[INITIAL_TRIP_CAPACITY][INITIAL_STOP_CAPACITY];
    }

    /** 
    * Asettaa aikatauluun ajan. Jos aika asetetaan olemassaolevan aikataulun
    * ulkopuolelle, aikataulua laajennetaan niin, että aika juuri ja juuri
    * mahtuu aikatauluun.
    * @param tripIdx Lähtö (sarake), johon aika asetetaan.
    * @param stopIdx Pysäkki (rivi), jolle aika asetetaan.
    * @param time    Asetettava aika.
    */
    public void setTime(int tripIdx, int stopIdx, short time) {
        if (tripIdx > tripCapacity-1 || stopIdx > stopCapacity-1) {
            enlargeTimetable(tripIdx+1, stopIdx+1);
        }
        if (tripIdx > numOfTrips-1) {
            numOfTrips = tripIdx+1;
        }
        if (stopIdx > numOfStops-1) {
            numOfStops = stopIdx+1;
        }
        timetable[tripIdx][stopIdx] = time;
    }
    
    /** 
    * Lukee aikataulusta ajan. Jos aika luetaan olemassaolevan aikataulun
    * ulkopuolelle, ei ole määritelty, mitä tapahtuu.
    * @param tripIdx Lähtö (sarake), jolta aika luetaan.
    * @param stopIdx Pysäkki (rivi), jolta aika luetaan.
    * @return    	 Luettu aika.
    */
    public short getTime(int tripIdx, int stopIdx) {
        return timetable[tripIdx][stopIdx];
    }
    
    /** 
    * Etsii ensimmäisen lähdön (sarakkeen), jolla tietty aika esiintyy.
    * @param stopIdx Pysäkki (rivi), jolta aikaa etsitään.
    * @param time    Aika, jota etsitään.
    * @return    	 Sarake, jolta aika löytyy, tai -1, jos aikaa ei löydy.
    */
    public int searchTrip(int stopIdx, short time) {
        int tripIdx = 0;
        while (tripIdx < numOfTrips && getTime(tripIdx, stopIdx) != time) {
            tripIdx++;
        }
        if (tripIdx == numOfTrips) {
            return -1;
        } else {
            return tripIdx;
        }
            
    }

    /** 
    * Suurentaa aikataulutaulukkoa.
    * @param requiredTripCapacity Suurin vaadittava sarakenumero.
    * @param requiredStopCapacity Suurin vaadittava rivinumero.
    */
    private void enlargeTimetable(int requiredTripCapacity, int requiredStopCapacity) {
    	// Selvitetään, tarvitseeko taulukkoa laajentaa leveys- vai korkeus-
    	// suunnassa vai sekä että. Se tietohan ei tule kutsuvasta metodista.
        int newTripCapacity, newStopCapacity;
        if (requiredTripCapacity > tripCapacity) {
            newTripCapacity = requiredTripCapacity;
        } else {
            newTripCapacity = tripCapacity;
        }
        if (requiredStopCapacity > stopCapacity) {
            newStopCapacity = requiredStopCapacity;
        } else {
            newStopCapacity = stopCapacity;
        }
        // Tehdään uusi taulukko, ja kopioidaan ajat uuteen taulukkoon sarake
        // kerrallaan. Koko taulukkoa ei voi kopioida sellaisenaan, koska
        // sarakkeet saattavat olla eri korkuisia vanhassa ja uudessa 
        // taulukossa.
        short[][] enlarged = new short[newTripCapacity][newStopCapacity];
        for (int i=0; i < numOfTrips; i++) {
            System.arraycopy(timetable[i], 0, enlarged[i], 0, numOfStops);
        }
        // Korvataan vanha taulukko uudella ja vanhat ulottuvuudet uusilla.
        timetable = enlarged;
        tripCapacity = newTripCapacity;
        stopCapacity = newStopCapacity;
    }
    
    /** 
    * Palauttaa aikataulutaulukossa olevien lähtöjen eli sarakkeiden määrän.
    * @return Sarakkeiden määrä.
    */
    public int getNumOfTrips() {
        return numOfTrips;
    }
    
    /** 
    * Tallentaa aikataulutaulukon XML-tiedostoon.
    */
    public void saveXml(XmlSerializer xml) throws java.io.IOException {
        StringBuilder sb = new StringBuilder();
        for (int tripIdx = 0; tripIdx < getNumOfTrips(); tripIdx++) {
            XmlHelper.indent(xml);
            xml.startTag("", "trip");
            sb.setLength(0); // clear previous string
            for (int stopIdx = 0; stopIdx < numOfStops; stopIdx++) {
                sb.append(getTime(tripIdx, stopIdx)+",");
            }
            sb.deleteCharAt(sb.length()-1); // remove last comma
            xml.text(sb.toString());
            xml.endTag("", "trip");
            XmlHelper.newline(xml);
        }
    }
    
    /** 
    * Lukee aikataulutaulukon XML-tiedostosta.
    */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException {
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("trips")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("trip")) {
                int tripIdx = numOfTrips;
                xml.xpp.next();
                String[] times = xml.xpp.getText().split(",");
                for (int i = 0; i < times.length; i++) {
                    setTime(tripIdx, i, (short) Integer.parseInt(times[i]));
                }
                xml.xpp.next();
            } else {
                System.out.println("Unknown tag in trips! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
        
    }
    
}
