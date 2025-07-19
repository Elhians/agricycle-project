import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';

export interface IUtilisateur {
  id?: number;
  phone?: string;
  role?: keyof typeof UserRole;
  dateInscription?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUtilisateur> = {};
