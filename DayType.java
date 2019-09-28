package helbtrafficdata;

/**
* <p>DayType on yhden päivätyypin sisältävä luokka.</p>
*  
* <p>HSL:n aikatauludata on jaettu päivätyyppeihin. Maanantaista torstaihin
* on yhdet aikataulut, perjantaina toiset ja niin edelleen. Lisäksi 
* erikoispäiville on omat päivätyyppinsä, mutta tämä luokka ei puutu niihin.</p>
*
* <p>Tämä luokka tarjoaa yhdenmukaisen tavan viitata eri päivätyyppeihin.</p>
*
* <p>Jatkossa luokka saatetaan muuttaa sellaiseksi, ettei jokainen sitä kutsuva
* taho saa omia instanssejaan, vaan luokka luo valmiiksi tarvittavan määrän
* päivätyyppejä ja palauttaa vain viittauksia niihin. Tämä vähentää hieman
* muistin käyttöä.</p>
*
* @author Tuomas Lehti
* @version 2019-05-15
*/
public class DayType implements Comparable<DayType>
{
	/** Tämän instanssin päivätyyppi kokonaislukuna. */
    private int daytype;
    
    /** 
     * Tekstin ja kokonaislukujen välisissä muunnoksissa käytettävä 
     * apumerkkijono. 
     */
    private static final String daytypeString = "MaPeLaSu";
    
	/** 
	* Luo päivätyypistä uuden instanssin.
	* @param daytype Päivätyyppi kokonaislukuna.
	*/
    public DayType(int daytype) {
        setI(daytype);
    }
    
	/** 
	* Luo päivätyypistä uuden instanssin.
	* @param daytype Päivätyyppi merkkijonona.
	*/
    public DayType(String daytype) {
        setS(daytype);
    }

	/** 
	* Asettaa päivätyypin kokonaisluvusta.
	* @param daytype Päivätyyppi kokonaislukuna.
	*/
    public void setI(int daytype) {
        this.daytype = daytype;
    }
    
	/** 
	* <p>Asettaa päivätyypin merkkijonosta.</p>
	* @param daytype Päivätyyppi merkkijonona.
	*/
    public void setS(String daytype) {
        this.daytype = daytypeString.indexOf(daytype)/2;
    }
    
    /**
    * @return Päivätyyppi kokonaislukuna.
    */
    public int getI() {
        return daytype;
    }
    
    /**
    * @return Päivätyyppi kokonaislukuna.
    */
    public String getS() {
        return daytypeString.substring(daytype*2, daytype*2+2);
    }
    
    @Override
    public int compareTo(DayType other) {
        return this.getI() - other.getI();
    }
    
    @Override
    public boolean equals(Object other) {
    	if (other == this) {
    		return true;
    	}
    	if (!(other instanceof DayType)) {
    		return false;
    	}
    	DayType otherDayType = (DayType) other;
    	return this.getI() == otherDayType.getI();
    }
}
