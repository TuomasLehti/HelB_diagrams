
/**
 * <p>RouteIdAndDir on HSL:n reittitunnuksia käsittelvä luokka.</p>
 * 
 * <p>HSL:n reittitunnukset ovat merkkijonoja, jotka koostuvat 4-6 merkistä:</p>
 * <ul>
 *      <li>1. merkki: Kaupunkikoodi (1 = Helsinki, 2 = Espoo, 5 = poikittaislinja jne).</li>
 *      <li>2.-5. merkki: Asiakkaille näkyvä linjatunnus, joka koostuu enintään kolmesta 
 *          numerosta ja Helsingin Bussiliikeneen tapauksessa mahdollisesti yhdestä
 *          kirjaimista. Numero-osa alkaa nollilla, jos kyseessä on kaksi- tai yksinumeroinen
 *          linjanumero.</li>
 *      <li>6. merkki: Mahdollinen versio linjasta, esimerkiksi isojen päätepysäkkisilmukoiden
 *          hännät erotellaan versionumerolla.</li>
 * </ul>
 * 
 * <p>HSL:n reittitunnuksen perään lisätään vielä pieni s-kirjain, ja suunta, joka ilmaistaan
 * yhdellä merkillä. 1 = maalle päin ja 2 = keskustaan päin.</p>
 *
 * @author Tuomas Lehti
 * @version 2019-03-07
 */
public class RouteIdAndDir implements Comparable<RouteIdAndDir>
{

    private String id;
    private String dir;
    
    /** 
     * Luo uuden objektin. 
     * @param idAndDir Koko reittitunnusmerkkijono suuntineen päivineen (esim. 1018Ns1).
     */
    public RouteIdAndDir(String idAndDir) {
        id = idAndDir.substring(0, idAndDir.indexOf("s"));
        dir = idAndDir.substring(idAndDir.indexOf("s")+1);
    }
    
    /** 
     * Luo uuden objektin. 
     * @param id    Reittitunnus (esim. 1014 tai 2551N).
     * @param dir   Suunta (1 tai 2).
     */
    public RouteIdAndDir(String id, String dir) {
        this.id = id;
        this.dir = dir;
    }
    
    /**
     * @return  Reittitunnus (esim. 1023N).
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return  Suunta (1 tai 2).
     */
    public String getDir() {
        return dir;
    }
    
    /**
     * @return Koko reittitunnusmerkkijono suuntineen päivineen (esim. 1018Ns1).
     */
    public String get() {
        return id + "s" + dir;
    }
    
    /**
     * Palauttaa lajiteltavan version reittitunnuksesta suuntineen.<p>
     * 
     * Perusreittitunnukset järjestyvät sellaisinaan kaupunkikoodin mukaan. Useimmiten 
     * kuitenkin halutaan järjestää ne matkustajille näkyvän linjanumeron perusteella, eli
     * ilman kaupunkikoodia.<p>
     * 
     * Toisekseen reittitunnusten pituus vaikuttaa järjestymiseen, eli 1018N saattaa
     * järjestyä ennen 1018:aa. Tästä efektistä päästään pidentämällä tunnus 
     * maksimipituiseksi.
     * 
     * @return Reittitunnus ilman kaupunkikoodia ja maksimimittaisena, suuntamerkintöineen.
     */
    public String getSortableIdAndDir() {
        StringBuilder sb = new StringBuilder(id.substring(1));
        // Stringbuilder.setLength lisää nollamerkkejä merkkijonon perään, jos se on liian 
        // lyhyt, ja nyt halutaan välilyöntejä. Tehdään siis ensin merkkijonosta liian pitkä,
        // ja sen jälkeen setLength voi vain katkaista merkkijonon oikeasta kohdasta.
        sb.append("         ");
        sb.setLength(6);
        sb.append("s"+dir);
        return sb.toString();
    }

    /**
     * Vertaa kahta reittitunnusobjektia keskenään. @see #getSortableIdAndDir
     */
    @Override
    public int compareTo(RouteIdAndDir other) {
        return getSortableIdAndDir().compareTo(other.getSortableIdAndDir());
    }

    /**
     * <p>Palauttaa ruudulla näytettävän version linjatunnuksesta ilman suuntaa.</p>
     * 
     * <p>Helsingin Bussiliikenteen tapauksessa ei enää ole olemassa samoja linjanumeroita
     * eri kaupungeissa. Siksi tätä luokkaa käyttävien ohjelmien käyttäjille voidaan näyttää
     * pelkkä linjanumero ilman kaupunkikoodia ja linjaversiota. Myös etunollat jätetään
     * tietenkin näyttämättä.</p>
     * 
     * @return Reittitunnus ilman etunollia, linjaversiota ja suuntamerkintöjä.
     */
    public String getScreenId() {
        StringBuilder sb = new StringBuilder(id.substring(1));
        char padding = "0".charAt(0);
        while (sb.charAt(0) == padding) {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }
    
}
