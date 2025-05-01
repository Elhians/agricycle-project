import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IPost {
  id?: number;
  contenu?: string;
  dateCreation?: dayjs.Dayjs;
  auteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IPost> = {};
