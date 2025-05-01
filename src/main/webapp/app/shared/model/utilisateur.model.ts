import dayjs from 'dayjs';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';

export interface IUtilisateur {
  id?: number;
  phone?: string;
  passwordHash?: string;
  email?: string | null;
  role?: keyof typeof UserRole;
  dateInscription?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IUtilisateur> = {};
