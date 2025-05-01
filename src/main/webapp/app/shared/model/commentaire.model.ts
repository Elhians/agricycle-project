import dayjs from 'dayjs';
import { IPost } from 'app/shared/model/post.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface ICommentaire {
  id?: number;
  contenu?: string;
  date?: dayjs.Dayjs;
  post?: IPost | null;
  auteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<ICommentaire> = {};
