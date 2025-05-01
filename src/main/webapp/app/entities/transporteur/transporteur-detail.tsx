import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transporteur.reducer';

export const TransporteurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transporteurEntity = useAppSelector(state => state.transporteur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transporteurDetailsHeading">
          <Translate contentKey="agriCycleApp.transporteur.detail.title">Transporteur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transporteurEntity.id}</dd>
          <dt>
            <span id="zoneCouverture">
              <Translate contentKey="agriCycleApp.transporteur.zoneCouverture">Zone Couverture</Translate>
            </span>
          </dt>
          <dd>{transporteurEntity.zoneCouverture}</dd>
          <dt>
            <span id="typeVehicule">
              <Translate contentKey="agriCycleApp.transporteur.typeVehicule">Type Vehicule</Translate>
            </span>
          </dt>
          <dd>{transporteurEntity.typeVehicule}</dd>
          <dt>
            <span id="capacite">
              <Translate contentKey="agriCycleApp.transporteur.capacite">Capacite</Translate>
            </span>
          </dt>
          <dd>{transporteurEntity.capacite}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.transporteur.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{transporteurEntity.utilisateur ? transporteurEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transporteur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transporteur/${transporteurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransporteurDetail;
