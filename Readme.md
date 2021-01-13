# PatraWeather
	Aplicație meteo.
	
## Cuprins
1. [Scurtă introducere](#introducere)
2. [Utilizare](#paragraf1)
    1. [Exemplu fișier intrare](#subparagraf)
3. [Diagrama de clase](#paragraf2)
3. [Realizator](#paragraf3)

## Scurtă introducere <a name="introducere"></a>
Repository-ul conține o aplicație Java ce oferă informații despre vreme pornind de la un fișier cu date despre diferite localități din diferite părți ale globului. Aplicația încarcă datele făcând request-uri către API-ul oferit de [OpenWeather](https://openweathermap.org) pentru informațiile necesare. Aplicația folosește [JavaFX](https://openjfx.io/) si [minimal-json](https://github.com/ralfstx/minimal-json). Interfața grafică este intuitivă și ușor de folosit.

## Utilizare <a name="paragraf1"></a>
Informațiile despre fiecare localitate în parte trebuie să se găsească în fișierul  **_src/main/resources/input.txt_**. Datele trebuie să fie în următoarea ordine: **ID**\t**Nume_Oraș**\t**Latitudine**\t**Longitudine**\t**Codul ISO al țării**. Unde \t reprezintă **tab**.

### Exemplu fișier intrare <a name="subparagraf"></a>
819827	        Razvilka	55.591667       37.740833	RU<br/>
524901	        Moscow	        55.752220       37.615555	RU<br/>
2973393	        Tarascon	43.805828       4.660280	FR<br/>
2986678	        Ploufragan	48.491409       -2.794580	FR<br/>

## Diagrama de clase <a name="paragraf2"></a>
![](UML/ClassDiagram.png?raw=true "ClassDiagram")


## Realizator <a name="paragraf3"></a>
Std. Pătrașcu Alin-Mihai - Grupa: C114A



