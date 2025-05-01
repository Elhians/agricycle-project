import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { TypeMedia } from 'app/shared/model/enumerations/type-media.model';

export interface IContenuEducatif {
  id?: number;
  titre?: string;
  description?: string | null;
  typeMedia?: keyof typeof TypeMedia;
  url?: string;
  datePublication?: dayjs.Dayjs | null;
  auteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IContenuEducatif> = {};
