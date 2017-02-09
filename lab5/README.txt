za n = 10, nalazi prakticki instantno. Za n = 100 oko 20-te generacije. Za 1000 rijetko i pronade
optimum.

Selekcijski pritisak nakon neke vrijednosti nema smisla povecavati jer se desava da smo trosimo
puno vise proracunavanja jer je zeljeni fitness veci - Primjer, za vrijednosti 10.0 i 30.0 nema
puno razlike.

Zanimljivo je da vjerovatnost mutacije jako utjecen na algoritam. Optimalni range je jako ogranicen
i morao sam dosta probavati da proradi znacajnije.

Od kombinacije izmedu turnirske selekcije i nasumicnog odabira najbolje je bilo 2 turnirske selekcije.
Dok sam odabri velicina turnira izmedu 2,3,5 nije znacajnije utjecao na rezultate(5 je marginalno
bolji ali s obzirom na vecu kolicinu sortiranja u svakoj generaiji neisplativo u konacnici) --
isprobavano za n = 100.

Cross - funkcionira na nacin da odredi neki nasumicni index prije kojeg u dijete kopira sve sa
prvog roditelja, te nakon to indeksa idu bitovi drugog roditelja.

Jednadzba koju sam korisito je samo varijacija sigmoid funkcije (1/(1+e^(-x - c))) - c je da
pomakne funkciju inicijalno jer krecemo od iteracije 0 do max.