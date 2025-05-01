import utilisateur from 'app/entities/utilisateur/utilisateur.reducer';
import agriculteur from 'app/entities/agriculteur/agriculteur.reducer';
import commercant from 'app/entities/commercant/commercant.reducer';
import transporteur from 'app/entities/transporteur/transporteur.reducer';
import consommateur from 'app/entities/consommateur/consommateur.reducer';
import organisation from 'app/entities/organisation/organisation.reducer';
import entreprise from 'app/entities/entreprise/entreprise.reducer';
import produit from 'app/entities/produit/produit.reducer';
import transaction from 'app/entities/transaction/transaction.reducer';
import contenuEducatif from 'app/entities/contenu-educatif/contenu-educatif.reducer';
import reaction from 'app/entities/reaction/reaction.reducer';
import notification from 'app/entities/notification/notification.reducer';
import chatbotSession from 'app/entities/chatbot-session/chatbot-session.reducer';
import post from 'app/entities/post/post.reducer';
import media from 'app/entities/media/media.reducer';
import commentaire from 'app/entities/commentaire/commentaire.reducer';
import qRCode from 'app/entities/qr-code/qr-code.reducer';
import paiement from 'app/entities/paiement/paiement.reducer';
import signalement from 'app/entities/signalement/signalement.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  utilisateur,
  agriculteur,
  commercant,
  transporteur,
  consommateur,
  organisation,
  entreprise,
  produit,
  transaction,
  contenuEducatif,
  reaction,
  notification,
  chatbotSession,
  post,
  media,
  commentaire,
  qRCode,
  paiement,
  signalement,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
