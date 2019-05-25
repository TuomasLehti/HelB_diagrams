package helbtrafficdata;

import java.util.Map;
import java.util.HashMap;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>Diagram sis‰lt‰‰ yhden vuoroaikataulun.</p>
 
 * <p>Vuoroaikataulu koostuu sek‰ linjasivuista, joiden aikana kuljetetaan 
 * matkustajia, ett‰ tyhj‰n‰ajosivuista, joiden aikana ei kuljeteta matkustajia.
 * Tyhj‰n‰ ajoa on kolmenlaista:</p>
 
 * <ul><li>Varikolta ensimm‰isen linjan ensimm‰iselle pys‰kille (ulosajo).</li>
 *     <li>Viimeisen linjan viimeiselt‰ pys‰kilt‰ varikolle (sis‰‰najo).</li>
 *     <li>Linjasivujen v‰liss‰ p‰‰tepys‰kilt‰ toiselle (siirtoajo).</li></ul>
 *
 * <p>Kummallekin sivutyypille on oma Mappinsa, joiden avaimena on sivun
 * j‰rjestysnumero. J‰rjestysnumerointi alkaa nollasta.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-05-25
 */

public class Diagram
{
	/** T‰m‰n vuoroaikataulun numero */
    int diagramId = 0;

    /** 
     * Vuoroaikataulun koko, toisin sanoen tyhj‰n‰ajosivujen ja 
     * linjasivujen m‰‰r‰ yhteens‰.
     */
    int numOfRows = 0;

    /** Linjasivut s‰ilˆt‰‰n Mappiin. Avain on rivinumero ja arvo on itse sivu. */
    Map<Integer,TripOfADiagram> trips = new HashMap<Integer,TripOfADiagram>();
    
    /** Tyhj‰n‰ajosivut s‰ilˆt‰‰n Mappiin. Avain on rivinumero ja arvo on itse sivu. */
    Map<Integer,DeadheadOfADiagram> deadheads = new HashMap<Integer,DeadheadOfADiagram>();
    
    /**
     * @return Vuoroaikataulun linja- ja tyhj‰n‰ajosivujen m‰‰r‰ yhteens‰.
     */
    public int getNumOfRows() {
        return numOfRows;
    }
    
    /**
     * @return Vuoroaikataulun numero.
     */
    public int getDiagramId() {
        return diagramId;
    }
    
    /** Asettaa vuoroaikataulun numeron. */
    public void setDiagramId(int id) {
        diagramId = id;
    }
    
    /**
     * Palauttaa tiedon, onko tietty rivi linjasivu.
     * @param index Rivin j‰rjestysnumero.
     * @return Tosi, jos rivi on linjasivu. False, jos rivi on tyhj‰n‰ajosivu.
     */
    public boolean isTrip(int index) {
        return trips.containsKey(index);
    }
    
    /**
     * Palauttaa linjasivun. Jos kysell‰‰n rivi‰, joka ei ole linjasivu,
     * palauttaa null.
     * @param index Rivin j‰rjestysnumero.
     * @return Haluttu linjasivu tai null.
     */
    public TripOfADiagram getTrip(int index) {
        if (isTrip(index)) {
            return trips.get(index);
        } else {
            return null;
        }
    }
    
    /**
     * Lis‰‰ linjasivun listaan.
     * @param trip Lis‰tt‰v‰ sivu.
     */
    public void addTrip(TripOfADiagram trip) {
        trips.put(numOfRows, trip);
        numOfRows++;
    }
    
    /**
     * Palauttaa tyhj‰n‰ajosivun. Jos kysell‰‰n rivi‰, joka ei ole tyhj‰n‰ajo,
     * palauttaa null.
     * @param index Rivin j‰rjestysnumero.
     * @return Haluttu tyhj‰n‰ajosivu tai null.
     */
    public DeadheadOfADiagram getDeadhead(int index) {
        if (!isTrip(index)) {
            return deadheads.get(index);
        } else {
            return null;
        }
    }

    /**
     * Lis‰‰ tyhj‰n‰ajosivun listaan.
     * @param trip Lis‰tt‰v‰ sivu.
     */
    public void addDeadhead(DeadheadOfADiagram deadhead) {
        deadheads.put(numOfRows, deadhead);
        numOfRows++;
    }

    /** Tallentaa vuoroaikataulun xml-tiedostoon. */
    public void saveXml(XmlHelper xml) throws java.io.IOException, XmlPullParserException {
        xml.indent();
        xml.serializer.startTag("", "diagram");
        xml.serializer.attribute("", "id", Integer.toString(diagramId));
        xml.newline();
        for (int idx = 0; idx < getNumOfRows(); idx++) {
            if (isTrip(idx)) {
                getTrip(idx).saveXml(xml);
            } else {
                getDeadhead(idx).saveXml(xml);
            }
        }
        
        xml.iEndTagLn("diagram");
    }
    
    /** Lataa vuoroaikataulun xml-tiedostosta. */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException
    {
        
        diagramId = Integer.parseInt(xml.xpp.getAttributeValue(0));
//        System.out.println("Reading diagram "+diagramId);
        
        numOfRows = 0;
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag("diagram")) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals("trip")) {
                xml.xpp.next();
                TripOfADiagram trip = new TripOfADiagram();
                trip.loadXml(xml);
                trips.put(numOfRows, trip);
                numOfRows++;
                xml.xpp.next();
            } else if (xml.xpp.getName().equals("deadhead")) {
                DeadheadOfADiagram deadhead = new DeadheadOfADiagram();
                deadhead.loadXml(xml);
                deadheads.put(numOfRows, deadhead);
                numOfRows++;
                xml.xpp.next();
            } else {
                System.out.println("Unknown tag in a diagram! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }
   
}
