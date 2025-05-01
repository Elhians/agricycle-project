import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './produit.reducer';

export const ProduitDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const produitEntity = useAppSelector(state => state.produit.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="produitDetailsHeading">
          <Translate contentKey="agriCycleApp.produit.detail.title">Produit</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{produitEntity.id}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="agriCycleApp.produit.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{produitEntity.nom}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="agriCycleApp.produit.description">Description</Translate>
            </span>
          </dt>
          <dd>{produitEntity.description}</dd>
          <dt>
            <span id="prix">
              <Translate contentKey="agriCycleApp.produit.prix">Prix</Translate>
            </span>
          </dt>
          <dd>{produitEntity.prix}</dd>
          <dt>
            <span id="quantite">
              <Translate contentKey="agriCycleApp.produit.quantite">Quantite</Translate>
            </span>
          </dt>
          <dd>{produitEntity.quantite}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="agriCycleApp.produit.type">Type</Translate>
            </span>
          </dt>
          <dd>{produitEntity.type}</dd>
          <dt>
            <span id="statut">
              <Translate contentKey="agriCycleApp.produit.statut">Statut</Translate>
            </span>
          </dt>
          <dd>{produitEntity.statut}</dd>
          <dt>
            <span id="imageUrl">
              <Translate contentKey="agriCycleApp.produit.imageUrl">Image Url</Translate>
            </span>
          </dt>
          <dd>{produitEntity.imageUrl}</dd>
          <dt>
            <span id="dateAjout">
              <Translate contentKey="agriCycleApp.produit.dateAjout">Date Ajout</Translate>
            </span>
          </dt>
          <dd>{produitEntity.dateAjout ? <TextFormat value={produitEntity.dateAjout} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.produit.vendeur">Vendeur</Translate>
          </dt>
          <dd>{produitEntity.vendeur ? produitEntity.vendeur.id : ''}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.produit.acheteur">Acheteur</Translate>
          </dt>
          <dd>{produitEntity.acheteur ? produitEntity.acheteur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/produit" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/produit/${produitEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProduitDetail;
