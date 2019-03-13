# HelB_diagrams

<p>Helsingin Bussiliikenteen vuoroaikatauluja xml-muodossa käsitteleviä luokkia.</p>

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
