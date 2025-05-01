import dayjs from 'dayjs';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { TypeProduit } from 'app/shared/model/enumerations/type-produit.model';
import { StatutAnnonce } from 'app/shared/model/enumerations/statut-annonce.model';

export interface IProduit {
  id?: number;
  nom?: string;
  description?: string | null;
  prix?: number;
  quantite?: number;
  type?: keyof typeof TypeProduit;
  statut?: keyof typeof StatutAnnonce;
  imageUrl?: string | null;
  dateAjout?: dayjs.Dayjs | null;
  vendeur?: IUtilisateur | null;
  acheteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<IProduit> = {};
