export interface IMyError {
    // interfejs za prikaz errora (klijent i server errori)
    // message imaju oba tipa greske i to jedino obavezno vadim iz error objekta za logovanje ili 
    // notifikaciju korisnika
    message : string;
    // ova 4 imaju serverske greske (tako sam napravio u spring boot), ali cu koristiti samo subErrors za notifikaciju
    // takva notifikacija vec postoji pri loginu i registraciji , pa ce ovo biti za SVE errore
    // debugmessage? : any;
    // status? : string;
    subErrors? : string[];
    // timestamp? : string;
    // ako je klijentska greska moze i stack da se prikaze (nikad u prod , korisno u dev uz message )
    stack? : any;

}


// VAZNA NAPOMENA : Interfejsi ne postoje u javasript tj. pri kompajliranju ts u js fajl se ignorisu
// Korisni su samo u ts jer omogucavaju rad sa tipom podataka ( type je taj interfejs ) sto omogucava
// autokomplet, intelisense ... , cime sprecavaju greske (kompajler ts->js) ih koristi.
// ALI AKO STVARNO hocemo da koristimo objekat tog tipa interfejs nije dovoljan - MORA class 
// Uvek ako pravimo objekat tog tipa sa new MORA interfejs biti IMPLEMENTIRAN kao dole.

export class MyError implements IMyError {
    constructor(public message: string , public subErrors? : string[] , public stack? : any ) {}
}