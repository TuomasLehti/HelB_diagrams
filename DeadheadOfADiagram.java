package helbtrafficdata;

import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * <p>Sis‰lt‰‰ yhden tyhj‰n‰ajosivun.</p>
 *
 * <p>Tyhj‰n‰ajosivuissa on viisi eri tietokentt‰‰, joista osa voi olla tyhji‰:</p>
 * 
 * <p>Tyyppi, joka on aina t‰ytetty.</p>
 *
 * <p>L‰htˆpaikka. Ainoastaan ulosajon kohdalla t‰ll‰ on joku arvo. Siirto-
 * ja sis‰‰najossa l‰htˆpaikan pystyy p‰‰ttelem‰‰n edellisen linjasivun¥
 * viimeisest‰ pys‰kist‰.<p>
 *
 * <p>Saapumispaikka. Ainoastaan sis‰‰najon kohdalla t‰ll‰ on joku aro. Ulos-
 * ja siirtoajossa saapumispaikan pystyy p‰‰ttelem‰‰n seuraavan linjasivun
 * ensimm‰isest‰ pys‰kist‰.</p>
 *
 * <p>L‰htˆaika: Ulosajossa t‰ll‰ on arvo. Siirto- ja sis‰‰najossa arvon pystyy
 * p‰‰ttelem‰‰n edellisen linjasivun saapumisajasta.</p>
 *
 * <p>Saapumisaika: Sis‰‰najossa t‰ll‰ on varmasti arvo. Ulosajossa arvon
 * pystyy p‰‰ttelem‰‰n ensimm‰isen linjasivun l‰htˆajasta, josta on v‰hennetty
 * kolme minuuttia eli asiakaspalveluaika, mutta t‰m‰ kentt‰ saattaa silti olla
 * valmiiksi t‰ytetty. Siirtoajojen kohdalla voidaan toimia kuten ulosajoissa,
 * mutta siirtoajon ajoajan voi myˆs laskea vaikka Google Mapsista, jolloin 
 * arvona on todellinen saapumisaika.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-05-25
*/
public class DeadheadOfADiagram
{
    
    /** Tyhj‰n‰ajosivu varikolta ensimm‰iselle l‰htˆpys‰kilt‰. */
    public static String DEADHEAD_OUT = "out";

    /** Tyhj‰n‰ajosivu viimeiselt‰ p‰‰tepys‰kilt‰ varikolle. */
    public static String DEADHEAD_IN = "in";

    /** Tyhj‰n‰ajosivu kahden linjasivun v‰liss‰. */
    public static String DEADHEAD_EMPTY = "empty";
    
    /** Tyhj‰n‰ajosivun tyyppi. */
    private String deadheadType;
    
    /** Tyhj‰n‰ajosivun l‰htˆpaikka. */
    private String from;
    
    /** Tyhj‰n‰ajosivun p‰‰tepiste. */
    private String to;
    
    /** Tyhj‰n‰ajosivun l‰htˆaika. */
    private short departureTime = -1;
    
    /** Tyhj‰n‰ajosivun saapumisaika. */
    private short arrivalTime = -1;
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public void setArrivalTime(short arrTime) {
        this.arrivalTime = arrTime;
    }
    
    public void setDepartureTime(short depTime) {
        this.departureTime = depTime;
    }
    
    public String getFrom() {
        return from;
    }
    
    public String getTo() {
        return to;
    }
    
    public short getArrivalTime() {
        return arrivalTime;
    }
    
    public short getDepartureTime() {
        return departureTime;
    }
    
    public void setDeadheadType(String type) {
        deadheadType = type;
    }
    
    /** Palauttaa tiedon, onko t‰m‰ tyhj‰n‰ajo tietty‰ tyyppi‰. */
    public boolean isOfType(String type) {
        return deadheadType.equals(type);
    }

    /** XML-tagi, jonka sis‰ll‰ tyhj‰n‰ajosivuun liittyv‰t tagit ovat. */
    private static String TAG_DEADHEAD = "deadhead";
    
    /** Deadhead-tagin attribuutti, joka m‰‰ritt‰‰ tyhj‰n‰ajosivun tyypin. */
    private static String ATTR_DEADHEAD_TYPE = "type";
    
    /** XML-tagi, joka sis‰lt‰‰ l‰htˆajan. */
    private static String TAG_DEPARTURE_TIME = "dep_time";
    
    /** XML-tagi, joka sis‰lt‰‰ saapumisajan. */
    private static String TAG_ARRIVAL_TIME = "arr_time";
    
    /** XML-tagi, joka sis‰lt‰‰ l‰htˆpaikan. */
    private static String TAG_FROM = "from";
    
    /** XML-tagi, joka sis‰lt‰‰ saapumispaikan. */
    private static String TAG_TO = "to";
    
    /** Tallentaa tyhj‰n‰ajosivun XML-tiedostoon. */
    public void saveXml(XmlHelper xml) throws java.io.IOException, XmlPullParserException
    {
        xml.indent();
        xml.serializer.startTag("", TAG_DEADHEAD);
        xml.serializer.attribute("", ATTR_DEADHEAD_TYPE, deadheadType);
        xml.serializer.text("\t");

        if (this.departureTime > 0) {
            xml.serializer.startTag("", TAG_DEPARTURE_TIME);
            xml.serializer.text(Integer.toString(this.departureTime));
            xml.serializer.endTag("", TAG_DEPARTURE_TIME);
            xml.serializer.text("\t");
        }

        if (this.arrivalTime > 0) {
            xml.serializer.startTag("", TAG_ARRIVAL_TIME);
            xml.serializer.text(Integer.toString(this.arrivalTime));
            xml.serializer.endTag("", TAG_ARRIVAL_TIME);
            xml.serializer.text("\t");
        }

        if (this.from != null && !this.from.equals("")) {
            xml.serializer.startTag("", TAG_FROM);
            xml.serializer.text(this.from);
            xml.serializer.endTag("", TAG_FROM);
            xml.serializer.text("\t");
        }

        if (this.to != null && !this.to.equals("")) {
            xml.serializer.startTag("", TAG_TO);
            xml.serializer.text(this.to);
            xml.serializer.endTag("", TAG_TO);
            xml.serializer.text("\t");
        }
        
        xml.serializer.endTag("", TAG_DEADHEAD);
        xml.newline();
    }

    /** Lataa tyhj‰n‰ajosivun XML-tiedostosta. */
    public void loadXml(XmlPullHelper xml) throws java.io.IOException, XmlPullParserException
    {
        deadheadType = xml.xpp.getAttributeValue(0);
        xml.xpp.next();
        while (!xml.atEndOfDocument() && !xml.atEndTag(TAG_DEADHEAD)) {
            // Find next start tag.
            while (!xml.atEndOfDocument() && !xml.atStartTag()) {
                xml.xpp.next();
            }
            if (xml.xpp.getName().equals(TAG_DEPARTURE_TIME)) {
                xml.xpp.next();
                this.departureTime = (short) Integer.parseInt(xml.xpp.getText());
                xml.xpp.next();
            } else if (xml.xpp.getName().equals(TAG_ARRIVAL_TIME)) {
                xml.xpp.next();
                this.arrivalTime = (short) Integer.parseInt(xml.xpp.getText());
                xml.xpp.next();
            } else if (xml.xpp.getName().equals(TAG_FROM)) {
                xml.xpp.next();
                this.from = xml.xpp.getText();
                xml.xpp.next();
            } else if (xml.xpp.getName().equals(TAG_TO)) {
                xml.xpp.next();
                this.to = xml.xpp.getText();
                xml.xpp.next();
            } else {
                System.out.println("Unknown tag in a deadhead of a diagram! ("+xml.xpp.getName()+")");
            }
            xml.xpp.next();
            while (!xml.atEndOfDocument() && !xml.atATag()) {
                xml.xpp.next();
            }
        }
    }
}
