package helbtrafficdata;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * XmlPullHelper is a helper class of XmlPullParser.
 * 
 * Some of the functionality could be achieved via XmlPullParser's own require-function.
 *
 * @author Tuomas Lehti
 * @version 2019-05-12
 */
public class XmlPullHelper
{

    public XmlPullParser xpp = null;
    
    /**
     * Constructs an object of class XmlPullHelper
     * @param  xpp      The XmlPullParser which this helper is tied to. 
     *                  Must not be <code>null</code>.
     */
    public XmlPullHelper(XmlPullParser xpp)
    {
        this.xpp = xpp;
    }
    
    /** 
     * Checks if the parser is at a start tag and whether the start tag 
     * has a given name.
     * @param  name     The name of the tag.
     * @return          Returns true if both conditions are true.
     */
    public boolean atStartTag(String name) throws XmlPullParserException {
        return 
            xpp.getEventType() == XmlPullParser.START_TAG 
            && xpp.getName().equals(name);
    }

    /** Checks if the parser is at a start tag.
     * @return          Returns true if the parser is at a start tag.
     */
    public boolean atStartTag() throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.START_TAG;
    }

    /** 
     * Checks if the parser is at an end tag and whether the end 
     * tag has a given name.
     * @param  name     The name of the tag.
     * @return          Returns true if both conditions are true.
     */
    public boolean atEndTag(String name) throws XmlPullParserException {
        return 
            xpp.getEventType() == XmlPullParser.END_TAG 
            && xpp.getName().equals(name);
    }
    
    /** Checks if the parser is at an end tag.
     * @return          Returns true if the parser is at an end tag.
     */
    public boolean atEndTag() throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_TAG;
    }
    
    /** Checks if the parser is at a tag.
     * @return          Returns true if the parser is at a tag.
     */
    public boolean atATag() throws XmlPullParserException {
        return 
            xpp.getEventType() == XmlPullParser.START_TAG
            || xpp.getEventType() == XmlPullParser.END_TAG;
    }
    
    /** Checks if the parser is at the end of the document.
     * @return          Returns true if the parser is at the end of 
     					the document.
     */
    public boolean atEndOfDocument() throws XmlPullParserException {
        return xpp.getEventType() == XmlPullParser.END_DOCUMENT;
    }

}
