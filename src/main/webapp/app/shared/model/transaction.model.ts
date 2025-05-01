import dayjs from 'dayjs';
import { IProduit } from 'app/shared/model/produit.model';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { StatutPaiement } from 'app/shared/model/enumerations/statut-paiement.model';

export interface ITransaction {
  id?: number;
  date?: dayjs.Dayjs;
  statut?: keyof typeof StatutPaiement;
  montant?: number;
  produit?: IProduit | null;
  acheteur?: IUtilisateur | null;
}

export const defaultValue: Readonly<ITransaction> = {};
