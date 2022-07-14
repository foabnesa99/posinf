import dayjs from 'dayjs';
import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';

export interface ICena {
  id?: number;
  datumVazenja?: string | null;
  iznosCene?: number | null;
  robaIliUsluga?: IRobaIliUsluga | null;
}

export const defaultValue: Readonly<ICena> = {};
