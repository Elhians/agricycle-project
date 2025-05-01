import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './utilisateur.reducer';

export const UtilisateurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const utilisateurEntity = useAppSelector(state => state.utilisateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="utilisateurDetailsHeading">
          <Translate contentKey="agriCycleApp.utilisateur.detail.title">Utilisateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.id}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="agriCycleApp.utilisateur.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.phone}</dd>
          <dt>
            <span id="passwordHash">
              <Translate contentKey="agriCycleApp.utilisateur.passwordHash">Password Hash</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.passwordHash}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="agriCycleApp.utilisateur.email">Email</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.email}</dd>
          <dt>
            <span id="role">
              <Translate contentKey="agriCycleApp.utilisateur.role">Role</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.role}</dd>
          <dt>
            <span id="dateInscription">
              <Translate contentKey="agriCycleApp.utilisateur.dateInscription">Date Inscription</Translate>
            </span>
          </dt>
          <dd>
            {utilisateurEntity.dateInscription ? (
              <TextFormat value={utilisateurEntity.dateInscription} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/utilisateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/utilisateur/${utilisateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UtilisateurDetail;
