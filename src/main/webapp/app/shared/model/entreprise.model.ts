import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IEntreprise {
  id?: number;
  nomEntreprise?: string | null;
  typeActivite?: string | null;
  licence?: string | null;
  adressePhysique?: string | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IEntreprise> = {};
