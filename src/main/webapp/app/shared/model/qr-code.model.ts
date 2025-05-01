import dayjs from 'dayjs';
import { ITransaction } from 'app/shared/model/transaction.model';

export interface IQRCode {
  id?: number;
  valeur?: string;
  dateExpiration?: dayjs.Dayjs | null;
  transaction?: ITransaction | null;
}

export const defaultValue: Readonly<IQRCode> = {};
