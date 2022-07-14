import { ICena } from 'app/shared/model/cena.model';
import { IJedinicaMere } from 'app/shared/model/jedinica-mere.model';
import { IPoreskaKategorija } from 'app/shared/model/poreska-kategorija.model';
import { IStavkaFakture } from 'app/shared/model/stavka-fakture.model';

export interface IRobaIliUsluga {
  id?: number;
  naziv?: string | null;
  opis?: string | null;
  cenas?: ICena[] | null;
  jedinicaMere?: IJedinicaMere | null;
  poreskaKategorija?: IPoreskaKategorija | null;
  stavkaFaktures?: IStavkaFakture[] | null;
}

export const defaultValue: Readonly<IRobaIliUsluga> = {};
