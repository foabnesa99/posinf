import { IPoreskaStopa } from 'app/shared/model/poreska-stopa.model';
import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';

export interface IPoreskaKategorija {
  id?: number;
  naziv?: string | null;
  poreskaStopas?: IPoreskaStopa[] | null;
  robaIliUslugas?: IRobaIliUsluga[] | null;
}

export const defaultValue: Readonly<IPoreskaKategorija> = {};
