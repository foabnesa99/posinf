import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';

export interface IJedinicaMere {
  id?: number;
  naziv?: string | null;
  skraceniNaziv?: string | null;
  robaIliUslugas?: IRobaIliUsluga[] | null;
}

export const defaultValue: Readonly<IJedinicaMere> = {};
