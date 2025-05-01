import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './commercant.reducer';

export const CommercantDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commercantEntity = useAppSelector(state => state.commercant.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commercantDetailsHeading">
          <Translate contentKey="agriCycleApp.commercant.detail.title">Commercant</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.id}</dd>
          <dt>
            <span id="adresseCommerce">
              <Translate contentKey="agriCycleApp.commercant.adresseCommerce">Adresse Commerce</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.adresseCommerce}</dd>
          <dt>
            <span id="moyenPaiement">
              <Translate contentKey="agriCycleApp.commercant.moyenPaiement">Moyen Paiement</Translate>
            </span>
          </dt>
          <dd>{commercantEntity.moyenPaiement}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.commercant.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{commercantEntity.utilisateur ? commercantEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/commercant" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/commercant/${commercantEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommercantDetail;
