import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';
import { IPoslovnaGodina } from 'app/shared/model/poslovna-godina.model';

export interface IPreduzece {
  id?: number;
  pib?: string | null;
  naziv?: string | null;
  adresa?: string | null;
  izlaznaFakturas?: IIzlaznaFaktura[] | null;
  poslovnaGodinas?: IPoslovnaGodina[] | null;
}

export const defaultValue: Readonly<IPreduzece> = {};
