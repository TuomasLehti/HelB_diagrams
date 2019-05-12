package helbtrafficdata;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

/**
 * <p>Luokka avustamaan XML-tiedostojen kirjoittamista.</p>
 *
 * <p>Luokkaa voi käyttää joko staattisesti tai dynaamisesti. Staattisen
 * käyttömuodon yhteydessä luokalle välitetään varsinainen XML-kirjoittaja-
 * luokka parametrinä. Dynaamisesti luokkaa käytettäessä XML-kirjoittaja-
 * luokka välitetään tämän luokan konstruktorin kautta.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-05-12
 */
public class XmlHelper
{
    /** Varsinainen XML-kirjoittajaluokka */
    public XmlSerializer xml = null;
    /** Varsinainen XML-kirjoittajaluokka */
    public XmlSerializer serializer = null;
    
    public XmlHelper() {
    }
    
    /** Luo luokasta instanssin.
    * @param xml XML-kirjoittajaluokka, johon tämä luokka kirjoittaa.
    */
    public XmlHelper(XmlSerializer xml) {
        this.xml = xml;
        this.serializer = xml;
    }
    
    /** 
    * Lisää XML-tiedostoon sarkainmerkkejä. Merkkejä lisätään sisennystason
    * mukainen määrä, esimerkiksi kolmannella tasolla kolme kappaletta.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    */
    public static void indent(XmlSerializer xml) throws java.io.IOException
    {
        for (int i=0; i < xml.getDepth(); i++) {
            xml.text("\t");
        }
    }

    /** 
    * Lisää XML-tiedostoon sarkainmerkkejä. Merkkejä lisätään sisennystason
    * mukainen määrä, esimerkiksi kolmannella tasolla kolme kappaletta.
    */
    public void indent() throws java.io.IOException
    {
        for (int i=0; i < this.xml.getDepth(); i++) {
            this.xml.text("\t");
        }
    }
    
    /** 
    * Lisää XML-tiedostoon rivinvaihdon.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    */
    public static void newline(XmlSerializer xml)  throws java.io.IOException
    {
        xml.text("\n");
    }

    /** 
    * Lisää XML-tiedostoon rivinvaihdon.
    */
    public void newline()  throws java.io.IOException
    {
        this.xml.text("\n");
    }
    
    /** 
    * Kirjoittaa aloitustagin, tekstin, lopetustagin ja rivinvaihdon muodostaman
    * kokonaisuuden yhdelle XML-tiedoston riville oikeaan tasoon sisennettynä.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    * @param tag Tagin nimi.
    * @param test Tagien väliin kirjoitettava teksti.
    */
    public static void indentTagTextTagNewline(XmlSerializer xml, String tag, String text) 
    throws java.io.IOException
    {
        indent(xml);
        xml.startTag("", tag);
        xml.text(text);
        xml.endTag("", tag);
        xml.text("\n");
    }

    /** 
    * Kirjoittaa aloitustagin, tekstin, lopetustagin ja rivinvaihdon muodostaman
    * kokonaisuuden yhdelle XML-tiedoston riville oikeaan tasoon sisennettynä.
    * @param tag Tagin nimi.
    * @param test Tagien väliin kirjoitettava teksti.
    */
    public void iTagTextTagLn(String tag, String text) 
    throws java.io.IOException
    {
        indent();
        xml.startTag("", tag);
        xml.text(text);
        xml.endTag("", tag);
        xml.text("\n");
    }

    /** 
    * Kirjoittaa oikeaan tasoon sisennetyn aloitustagin ja rivinvaihdon 
    * XML-tiedostoon.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    * @param tag Tagin nimi.
    */
    public static void indentStartTagNewLine(XmlSerializer xml, String tag) throws java.io.IOException
    {
        indent(xml);
        xml.startTag("", tag);
        xml.text("\n");
        
    }

    /** 
    * Kirjoittaa oikeaan tasoon sisennetyn aloitustagin ja rivinvaihdon 
    * XML-tiedostoon.
    * @param tag Tagin nimi.
    */
    public void iStartTagLn(String tag) throws java.io.IOException
    {
        indent();
        xml.startTag("", tag);
        xml.text("\n");
    }

    /** 
    * Kirjoittaa oikeaan tasoon sisennetyn lopetustagin ja rivinvaihdon 
    * XML-tiedostoon.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    * @param tag Tagin nimi.
    */
    public static void indentEndTagNewLine(XmlSerializer xml, String tag) throws java.io.IOException
    {
        for (int i=0; i < xml.getDepth()-1; i++) {
            xml.text("\t");
        }
        xml.endTag("", tag);
        xml.text("\n");
        
    }

    /** 
    * Kirjoittaa oikeaan tasoon sisennetyn lopetustagin ja rivinvaihdon 
    * XML-tiedostoon.
    * @param tag Tagin nimi.
    */
    public void iEndTagLn(String tag) throws java.io.IOException
    {
        for (int i=0; i < xml.getDepth()-1; i++) {
            xml.text("\t");
        }
        xml.endTag("", tag);
        xml.text("\n");
        
    }
    
    /** 
    * Kirjoittaa oikeaan tasoon sisennetyn tekstipätkän ja rivinvaihdon 
    * XML-tiedostoon.
    * @param xml XML-kirjoittajaluokka, johon tämä metodi kirjoittaa.
    * @param tag XML-tiedostoon kirjoitettava teksti.
    */
    public static void indentTextNewLine(XmlSerializer xml, String tag) throws java.io.IOException
    {
        indent(xml);
        xml.text("tag");
        xml.text("\n");
        
    }
    
}