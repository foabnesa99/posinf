import kupac from 'app/entities/kupac/kupac.reducer';
import izlaznaFaktura from 'app/entities/izlazna-faktura/izlazna-faktura.reducer';
import robaIliUsluga from 'app/entities/roba-ili-usluga/roba-ili-usluga.reducer';
import cena from 'app/entities/cena/cena.reducer';
import poreskaKategorija from 'app/entities/poreska-kategorija/poreska-kategorija.reducer';
import poreskaStopa from 'app/entities/poreska-stopa/poreska-stopa.reducer';
import jedinicaMere from 'app/entities/jedinica-mere/jedinica-mere.reducer';
import poslovnaGodina from 'app/entities/poslovna-godina/poslovna-godina.reducer';
import stavkaFakture from 'app/entities/stavka-fakture/stavka-fakture.reducer';
import preduzece from 'app/entities/preduzece/preduzece.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  kupac,
  izlaznaFaktura,
  robaIliUsluga,
  cena,
  poreskaKategorija,
  poreskaStopa,
  jedinicaMere,
  poslovnaGodina,
  stavkaFakture,
  preduzece,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
