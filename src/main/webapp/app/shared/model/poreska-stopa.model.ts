import dayjs from 'dayjs';
import { IPoreskaKategorija } from 'app/shared/model/poreska-kategorija.model';

export interface IPoreskaStopa {
  id?: number;
  procenatPdv?: number | null;
  datumVazenja?: string | null;
  poreskaKategorija?: IPoreskaKategorija | null;
}

export const defaultValue: Readonly<IPoreskaStopa> = {};
