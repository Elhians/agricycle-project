import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { IPost } from 'app/shared/model/post.model';
import { TypeReaction } from 'app/shared/model/enumerations/type-reaction.model';
import { Cible } from 'app/shared/model/enumerations/cible.model';

export interface IReaction {
  id?: number;
  type?: keyof typeof TypeReaction;
  date?: dayjs.Dayjs | null;
  cible?: keyof typeof Cible;
  utilisateur?: IUtilisateur | null;
  post?: IPost | null;
}

export const defaultValue: Readonly<IReaction> = {};
