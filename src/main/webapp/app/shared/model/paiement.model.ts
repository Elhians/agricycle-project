import dayjs from 'dayjs';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IPaiement {
  id?: number;
  moyenPaiement?: string;
  preuve?: string | null;
  date?: dayjs.Dayjs | null;
  transaction?: ITransaction | null;
  acheteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IPaiement> = {};
