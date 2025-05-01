import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { TypeNotif } from 'app/shared/model/enumerations/type-notif.model';

export interface INotification {
  id?: number;
  type?: keyof typeof TypeNotif;
  contenu?: string | null;
  date?: dayjs.Dayjs | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<INotification> = {};
