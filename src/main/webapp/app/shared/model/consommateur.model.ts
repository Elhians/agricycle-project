import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IConsommateur {
  id?: number;
  preferences?: string | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IConsommateur> = {};
