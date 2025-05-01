import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IAgriculteur {
  id?: number;
  typeProduction?: string | null;
  anneeExperience?: number | null;
  localisation?: string | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IAgriculteur> = {};
