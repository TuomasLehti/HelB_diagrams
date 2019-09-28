# HelB_diagrams

<p>Helsingin Bussiliikenteen vuoroaikatauluja xml-muodossa käsitteleviä luokkia.</p>

<h2>Tietojen yleinen rakenne</h2>

<p>Aikataulut ja vuoroaikataulut on järjestetty puumaiseen rakenteeseen.</p>
<ul>
  <li>HelbTrafficData on ylin taso.</li>
  <ul>
    <li>RouteArray sisältää varsinaisen HSL:ltä saatavan aikatauludatan. Jokaisen linjan kumpikin suunta on jaettu omaksi reitikseen. HelBTrafficData sisältää vain yhden RouteArrayn.</li>
    <ul>
      <li>Route sisältää yhden reitin aikatauludatan. Data on jaettu neljään aikataulutaulukkoon, jokaiselle päivätyypille omansa.</li>
      <ul>
        <li>TripTable, joita on Routen sisällä neljä, sisältää varsinaisen aikatauludatan.</li>
        <li>StopOfARoute, joita on Routen sisällä tarvittava määrä, sisältävät tiedon reitin pysäkeistä.</li>
      </ul>
    </ul>
    <li>DiagramArray sisältää Helsingin bussiliikenteen vuoroaikataulut, siis kunkin bussin työpäivät. DiagramArray-objekteja on HelbTrafficDatan sisällä neljä, yksi kullekin päivätyypille.</li>
    <ul>
      <li>Diagram sisältää yhden vuoroaikataulun.</li>
      <ul>
        <li>DeadheadOfADiagram sisältää yhden tyhjänäajosivun.</li>
        <li>TripOfADiagram sisältää viittauksen yhteen aikatauludatan linjasivuun.</li>
      </ul>
    </ul>
  </ul>
</ul>

<h2>Reittitunnuksista</h2>

<p>HSL:n reittitunnukset ovat merkkijonoja, jotka koostuvat 4-6 merkistä:</p>
<ul>
<li>1. merkki: Kaupunkikoodi (1 = Helsinki, 2 = Espoo, 5 = poikittaislinja jne).</li>
<li>2.-5. merkki: Asiakkaille näkyvä linjatunnus, joka koostuu enintään kolmesta numerosta ja Helsingin Bussiliikeneen tapauksessa mahdollisesti yhdestä kirjaimista. Numero-osa alkaa nollilla, jos kyseessä on kaksi- tai yksinumeroinen linjanumero.</li>
<li>6. merkki: Mahdollinen versio linjasta, esimerkiksi isojen päätepysäkkisilmukoiden hännät erotellaan versionumerolla.</li>
</ul>

<h2>Välipistepysäkeistä</h2>

<p>Reittien varsilla on mm. HSL:n määrittelemiä välipistepysäkkejä ja HelB:n kuljettajanvaihtopysäkkejä. Näitä kutsutaan eri nimellä kuin mikä on pysäkin virallinen nimi.</p>
<p>Välipistenimet on jaettu eri luokkiin käyttötarkoituksen mukaan, jotta esimerkiksi HelBin kuljettajanvaihtopysäkkien eivät sekoitu Facebook-ajosarjapäivitysten lähtöpysäkkien nimiin.</p>

<h2>Ajoista</h2>

<p>Aikataulutaulukko tallentaa ajat kokonaislukuina, jonka muoto on kompromissi pienen muistijäljen (short eli 2 tavua per aika) ja ihmisluettavuuden kannalta. Luvun ykköset ja kymmenet ilmoittavat minuuttiluvun, ja sadat ja tarvittaessa tuhannet tunnin. Esimerkiksi puolipäivä merkitään 1200 ja varttia yli yhdeksän 945.</p>

<h2>XML:n lukemisesta ja kirjoittamisesta</h2>

<p>Luokat sisältävät metodit, joilla vuoroaikataulut saa luettua ja kirjoitettua XML-muodossa. Luokkien ja XML-tasojen suhde menee yksi yhteen, eli jokaista luokkaa vastaa yksi XML-tagi, jonka sisälle luokka kirjoittaa oman datansa.</p>
<p>Käytännössä jokaisessa trafficdata-puun luokassa on XML-kirjoittaja- ja -lukijametodi. Tällä hetkellä niitä ei pakoteta millään tavalla, esimerkiksi interfacella, mutta se on yksi kehityspolku, jota voinee pohtia jatkossa.</p>
<p>Kirjoitettaessa jokainen luokka olettaa, että sen vanhempi on jättänyt kirjoittajaluokan uuden rivin alkuun.</p>
<p>Luettaessa jokaisen luokan on oletettava, että sen vanhempi on jättänyt lukijaluokan tämän luokan aloitustagiin. Jokaisen luokan on jätettävä lukijaluokka oman luokan lopetustagiin.</p>
<p>XML:n kirjoitus ja luku käyttää kahta XML-avustajaluokkaa, joista XmlHelper kirjoittaa XML-tiedostoja ja XmlPullHelper lukee niitä. Näistä varsinkin XmlHelper on varsin sekava, ja XML-luku- ja -kirjoitusmetodeja pitäisikin selkeyttää.</p>

<h2>Todo</h2>

<ul>
  <li>XmlHelper: Staattisten metodien poistaminen luokasta. Vaatii paljon muutoksia myös muuhun koodiin.</li>
  <li>TripTable: Mitä tapahtuu, jos luetaan aika aikataulutaulukon ulkopuolelta?</li>
  <li>Route: DAY_TYPES pois</li>
  <li>Route: Siirtyminen sisäisesti DayType-luokan palvelujen pariin.</li>
</ul>
