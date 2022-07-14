import dayjs from 'dayjs';
import { IKupac } from 'app/shared/model/kupac.model';
import { IPreduzece } from 'app/shared/model/preduzece.model';
import { IStavkaFakture } from 'app/shared/model/stavka-fakture.model';
import { StatusFakture } from 'app/shared/model/enumerations/status-fakture.model';

export interface IIzlaznaFaktura {
  id?: number;
  brojFakture?: string | null;
  datumIzdavanja?: string | null;
  datumValute?: string | null;
  ukupnaOsnovica?: number | null;
  ukupanRabat?: number | null;
  ukupanIznos?: number | null;
  status?: StatusFakture | null;
  kupac?: IKupac | null;
  preduzece?: IPreduzece | null;
  stavkaFaktures?: IStavkaFakture[] | null;
}

export const defaultValue: Readonly<IIzlaznaFaktura> = {};
