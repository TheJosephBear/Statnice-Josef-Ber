# Mobilní aplikace pro ukládání léků

## Jak spustit projekt
Projekt lze spustit pouze v android studiu (není build)
V android studio stiskněte tlačítko "Open" a vyberte soubor projektu
Nastavení projektu:
- API 25 ("Nougat"+ Android 7.1.1)
- Kotlin DSL

## Jak používat aplikaci
- Pomocí tlačítka "+" na hlavní obrazovce lze přidávat nové léky.
- Po vyplnění všech povinných polí formuláře a stisknutí tlačítka "Uložit" se lék přidá do seznamu.
- Každý vytvořený lék je zobrazen v kartě na hlavní obrazovce, kde ho lze také smazat červeným tlačítkem.
- Pomocí tlačítka "..." vpravo dole lze léky seřadit abecedně vzestupně (A→Z) nebo sestupně (Z→A).
- Všechny léky se ukládají do úložiště aplikace, takže se neztratí ani po vypnutí.
- Pokud je systémový čas 8:00, 12:00 nebo 20:00 a existuje lék s nastavenou touto dobou užívání, aplikace pošle notifikaci.
