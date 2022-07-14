import { IPreduzece } from 'app/shared/model/preduzece.model';

export interface IPoslovnaGodina {
  id?: number;
  godina?: number | null;
  zakljucena?: boolean | null;
  preduzece?: IPreduzece | null;
}

export const defaultValue: Readonly<IPoslovnaGodina> = {
  zakljucena: false,
};
