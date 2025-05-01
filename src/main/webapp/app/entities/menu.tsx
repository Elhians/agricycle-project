import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/utilisateur">
        <Translate contentKey="global.menu.entities.utilisateur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/agriculteur">
        <Translate contentKey="global.menu.entities.agriculteur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/commercant">
        <Translate contentKey="global.menu.entities.commercant" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transporteur">
        <Translate contentKey="global.menu.entities.transporteur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/consommateur">
        <Translate contentKey="global.menu.entities.consommateur" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/organisation">
        <Translate contentKey="global.menu.entities.organisation" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/entreprise">
        <Translate contentKey="global.menu.entities.entreprise" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/produit">
        <Translate contentKey="global.menu.entities.produit" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/transaction">
        <Translate contentKey="global.menu.entities.transaction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/contenu-educatif">
        <Translate contentKey="global.menu.entities.contenuEducatif" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/reaction">
        <Translate contentKey="global.menu.entities.reaction" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/notification">
        <Translate contentKey="global.menu.entities.notification" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/chatbot-session">
        <Translate contentKey="global.menu.entities.chatbotSession" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/post">
        <Translate contentKey="global.menu.entities.post" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/media">
        <Translate contentKey="global.menu.entities.media" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/commentaire">
        <Translate contentKey="global.menu.entities.commentaire" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/qr-code">
        <Translate contentKey="global.menu.entities.qrCode" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/paiement">
        <Translate contentKey="global.menu.entities.paiement" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/signalement">
        <Translate contentKey="global.menu.entities.signalement" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
