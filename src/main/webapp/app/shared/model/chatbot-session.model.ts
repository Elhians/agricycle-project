import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IChatbotSession {
  id?: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IChatbotSession> = {};
