import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './consommateur.reducer';

export const ConsommateurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const consommateurEntity = useAppSelector(state => state.consommateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="consommateurDetailsHeading">
          <Translate contentKey="agriCycleApp.consommateur.detail.title">Consommateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{consommateurEntity.id}</dd>
          <dt>
            <span id="preferences">
              <Translate contentKey="agriCycleApp.consommateur.preferences">Preferences</Translate>
            </span>
          </dt>
          <dd>{consommateurEntity.preferences}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.consommateur.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{consommateurEntity.utilisateur ? consommateurEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/consommateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/consommateur/${consommateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ConsommateurDetail;
