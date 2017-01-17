# Baza danych płatności 
##Serwer
Aplikacja serwer odpowiada za komunikację ze zdalną bazą danych MySQL oraz transport danych pomiędzy aplikacjami klienckimi. 
Do uruchomienia aplikacji wymagane jest środowisko java RE do pobrania ze strony producenta. 
###Uruchomienie 
Aby uruchomić serwer należy w zależności od systemu operacyjnego wywołać plik server.jar z poziomu konsoli z odpowiednimi parametrami bazy dannych MySQL. Nie podanie argumntów spowoduje połączenie z domyślną bazą danych. 
Przykład dla Linux:
```bash
java -jar server.jar adres_ip login hasło
```
Przykład dla Windows: 
```bash
java -jar server.jar adres_ip login hasło
```
###Komunikaty 
Aplikacja serwer nie posiada graficznego interfejsu użytkownika, komunikaty z działania są wyświetlane w wierszu poleceń. Apolikację można zakończuć poprzez zamknięcie terminala lub wpisanie Ctrl+x.

##Kilent 
Aplilacja klienta posiada interfejs graficzny umożliwiający podgląd, dodawanie, usuwanie i modyfikację tabel baz danych. Do poprawnego działania aplikacji klient wymagane jest wcześniejsze uruchomienie aplikacji serwer. 
###Uruchomienie 
W celu uruchomienia aplikacji na systemie Windows należy dwukrotnie kliknąć na plik client.jar. Natomiast w systemach Linux należy wywołać plik client.jar z poziomu konsoli ,znajdując się w folderze zawierającym plik client.jar, w następujący sposób:
```bash
java -jar client.jar
```
Podczas uruchamiania aplikacji pojawi się okno w którym należy wpisać adres ip serwera. Domyślnie jest to localhost, więc jeżeli serwer pracuje na maszynie na której uruchamiamy klienta, nie należy edytować tego pola. 
![](https://github.com/kamfor/PaymentOrganizer/blob/master/img/input.png)

###Dodawanie i usuwanie rekordów
Po poprawnym połączeniu z serwerem pojawi się główne okno aplikacji zawierające widoki tabel bazy danych. Na zakładce płatności w dolnej częsci okna znajdują się pola tekstowe służące do wprowadzania nowych rekordów. Przysisk **Dodaj rekord** powoduje sprawdzene poprawności danych w polach tekstowych i w przypadku ich poprawności doda element o wpisanych wartościach pól do bazy danych. W przypadku niepowodzenia, w górnej części onka pojawi się czerwony komunikat informujący o błędzie i konieczoności poprawy danych. W celu usunuęcia rekordu z bazy danych należy zaznaczyć wiersz do usunięcia klikając jednkrotnie w dowolne pole wiersza, po czym nacisnąc przycisk **Usuń rekord**

![*Okno programu widok tabeli płatności*](https://github.com/kamfor/PaymentOrganizer/blob/master/img/paymentPanel.png)
*Okno programu widok tabeli płatności*

![*Okno programu przykładowy komunikat o błędzie*](https://github.com/kamfor/PaymentOrganizer/blob/master/img/error.png)
*Okno programu przykładowy komunikat o błędzie*

Dodawanie i usuwanie rekordów dla pozostałych zakładek przebiega analogicznie, warto zwrócić uwagę na zalezności pomiedzy tabelami, nie da się usunąć rekordu który posiada zależności.

![*widok tabeli agentów*](https://github.com/kamfor/PaymentOrganizer/blob/master/img/agentPanel.png)
*widok tabeli agentów*

![*widok tabeli podmiotów*](https://github.com/kamfor/PaymentOrganizer/blob/master/img/subjectPanel.png)
*widok tabeli podmiotów*

###Edycja rekordów 
W celu edycji rekordu istniejącego w tabeli należy dwukrotnie kliknąć lewym przyciskiem myszy na pole w tabeli które chcemy edytować. Po wprowadzeniu danych naciśniięcie klawisza enter bądź kliknięcie w inną komórkę tabeli spowoduje walidację danych i w przypadku akceptacji zmiany zostaną zaakceptowane. W przypadku podania nieodpowiedniej wartości pola zostanie wyświetlony komunikat o błedzie. Niektóre pola tabeli nie mają możliwości edycji, są to pola których wartości zostały obliczone na podstawie zależności pomiędzy tabelami. 

###Sortowanie 
W celu posortowania tabeli według kolumn należy dwukrotnie kliknąć na nazwę kolumny według której chcemy posortować tabelę. 
Dodatkowo można zmieniać kolejność kolumn na ekranie, poprzez przeciągnięcie kolumny na wybrane miejsce. 

###Logika 
Możliwe jest uruchomienie wielu klientów jednocześnie, zmiany dokonane na jednym z kleintów są bezpośrednio przesyłane do pozostałych uruchomionch klietów. Przy każdorazowym dodaniu/usunięciu/edycji elemetu w tabeli płatności, obliczane są zmiany dla pozostałuch tabel. Kolumna **Należność** danego Agenta w tabeli Agenci jest sumą wartości z tabeli Płatności mających przypisane ID agenta. Podobnie kolumna **Rachunek** jest sumą wartości z tabeli płatności gdzie id podmiotu w tabeli płatności i posmiotów są sobie równe.  

###Zamknięcie 
Aby zakończyć pracę z aplikacją należy kliknąć lewym przyciskiem myszy na ikonę X w prawym górnym rogu. 






