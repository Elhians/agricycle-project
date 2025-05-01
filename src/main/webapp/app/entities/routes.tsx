import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Utilisateur from './utilisateur';
import Agriculteur from './agriculteur';
import Commercant from './commercant';
import Transporteur from './transporteur';
import Consommateur from './consommateur';
import Organisation from './organisation';
import Entreprise from './entreprise';
import Produit from './produit';
import Transaction from './transaction';
import ContenuEducatif from './contenu-educatif';
import Reaction from './reaction';
import Notification from './notification';
import ChatbotSession from './chatbot-session';
import Post from './post';
import Media from './media';
import Commentaire from './commentaire';
import QRCode from './qr-code';
import Paiement from './paiement';
import Signalement from './signalement';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="utilisateur/*" element={<Utilisateur />} />
        <Route path="agriculteur/*" element={<Agriculteur />} />
        <Route path="commercant/*" element={<Commercant />} />
        <Route path="transporteur/*" element={<Transporteur />} />
        <Route path="consommateur/*" element={<Consommateur />} />
        <Route path="organisation/*" element={<Organisation />} />
        <Route path="entreprise/*" element={<Entreprise />} />
        <Route path="produit/*" element={<Produit />} />
        <Route path="transaction/*" element={<Transaction />} />
        <Route path="contenu-educatif/*" element={<ContenuEducatif />} />
        <Route path="reaction/*" element={<Reaction />} />
        <Route path="notification/*" element={<Notification />} />
        <Route path="chatbot-session/*" element={<ChatbotSession />} />
        <Route path="post/*" element={<Post />} />
        <Route path="media/*" element={<Media />} />
        <Route path="commentaire/*" element={<Commentaire />} />
        <Route path="qr-code/*" element={<QRCode />} />
        <Route path="paiement/*" element={<Paiement />} />
        <Route path="signalement/*" element={<Signalement />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
