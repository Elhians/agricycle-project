import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface ICommercant {
  id?: number;
  adresseCommerce?: string | null;
  moyenPaiement?: string | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<ICommercant> = {};
