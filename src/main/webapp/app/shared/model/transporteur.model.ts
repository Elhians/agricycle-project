import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { TypeVehicule } from 'app/shared/model/enumerations/type-vehicule.model';

export interface ITransporteur {
  id?: number;
  zoneCouverture?: string | null;
  typeVehicule?: keyof typeof TypeVehicule | null;
  capacite?: number | null;
  utilisateur?: IUtilisateur | null;
}

export const defaultValue: Readonly<ITransporteur> = {};
