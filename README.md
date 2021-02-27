# Event Hunter

Este o aplicatie pentru Android, scrisa in Java.
Pentru stocarea datelor se utilizeaza Firebase.

## Descrierea generala a aplicatiei 

Aceasta aplicatie ofera atat posibilitatea organizarii unor evenimente, cat si sansa utilizatorilor obisnuiti de a fi la curent cu acestea. 
Exista doua tipuri de evenimente: singulare si repetabile.
Evenimentele singulare se stabilesc in urma unei discutii intre organizator si colaborator. Aceastea se desfasoara o la o data stabilita.
Evenimentele repetabile sunt create de catre organizator si marcate intr-un orar. Ele se desfasoara saptamanal in aceeasi zi si in acelasi interval de timp. Colaboratorii pot solicita sa participe la aceste evenimente, iar organizatorii ii pot programa in functie de aceste solicitari.

## Inregistrare 

Aplicatia functioneaza cu 3 tipuri de utilizatori: **organizator, colaborator si utilizator obisnuit.** 
Toate tipurile de utilizatori trebuie sa se inregistreze pentru a folosi aplicatia.

Pentru inregistrare se va introduce adresa de e-mail, parola, numele si se va alege tipul de utilizator.
Logarea se face cu e-mail si parola.  

De asemenea, toti vor avea o pagina de profil. Paginile de profil ale organizatorilor si colaboratorilor contin o sectiune de contact.

## Organizator

Un eveniment este creat prin completarea unui formular de catre organizator. Acest formular contine detalii specifice tipului de eveniment. Organizatorul poate edita detaliile unui eveniment viitor in cazul in care nu a fost efectuata deja o rezervare. 
Organizatorul ofera un orar cu evenimente repetabile. 
Toate evenimentele se afla intr-o lista.

## Colaborator

Colaboratorii pot vizualiza orarele cu evenimentele repetabile ale organizatorilor si pot solicita participarea la aceste evenimente. In urma unei discutii cu organizatorul, colaboratorul poate fi adaugat la evenimentul respectiv.

## Utilizator obisnuit

Utilizatorul obisnuit poate sa vizualizeze evenimentele disponibile, numarul de locuri pentru fiecare eveniment, profilul unui organizator sau colaborator. 
De asemenea, el poate efectua rezervari la evenimentele care mai au locuri disponibile. Aceste rezervari pot fi vizualizate in pagina de profil si descarcate.

## Aspect

Pagini existente:
- pagina de autentificare
- pagina de inregistrare
- pagina principala
- pagini de profil pentru toate tipurile de utilizator
- pagini pentru creare celor doua tipuri de evenimente

In pagina principala exista trei taburi, fiecare dintre ele continand o lista si o bara de cautare. Din lista aferenta evenimentelor se pot face rezervari, iar din celelalte liste se va putea ajunge in pagina de profil a unui organizator sau colaborator.
