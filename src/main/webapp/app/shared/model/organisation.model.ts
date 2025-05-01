import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IOrganisation {
  id?: number;
  nomOrganisation?: string | null;
  siteWeb?: string | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IOrganisation> = {};
