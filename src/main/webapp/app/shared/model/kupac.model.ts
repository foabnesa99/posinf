import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';

export interface IKupac {
  id?: number;
  pib?: string | null;
  mib?: string | null;
  naziv?: string | null;
  adresa?: string | null;
  telefon?: string | null;
  izlaznaFakturas?: IIzlaznaFaktura[] | null;
}

export const defaultValue: Readonly<IKupac> = {};
