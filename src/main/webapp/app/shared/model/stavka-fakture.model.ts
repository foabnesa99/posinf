import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';
import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';

export interface IStavkaFakture {
  id?: number;
  kolicina?: number | null;
  jedinicnaCena?: number | null;
  rabat?: number | null;
  procenatPDV?: number | null;
  iznosPDV?: number | null;
  ukupnaCena?: number | null;
  izlaznaFaktura?: IIzlaznaFaktura | null;
  robaIliUsluga?: IRobaIliUsluga | null;
}

export const defaultValue: Readonly<IStavkaFakture> = {};
