import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { IPost } from 'app/shared/model/post.model';
import { Cible } from 'app/shared/model/enumerations/cible.model';

export interface ISignalement {
  id?: number;
  raison?: string;
  cible?: keyof typeof Cible;
  date?: dayjs.Dayjs | null;
  auteur?: IUtilisateur | null;
  ciblePost?: IPost | null;
}

export const defaultValue: Readonly<ISignalement> = {};
