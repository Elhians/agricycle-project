import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './signalement.reducer';

export const SignalementDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const signalementEntity = useAppSelector(state => state.signalement.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="signalementDetailsHeading">
          <Translate contentKey="agriCycleApp.signalement.detail.title">Signalement</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{signalementEntity.id}</dd>
          <dt>
            <span id="raison">
              <Translate contentKey="agriCycleApp.signalement.raison">Raison</Translate>
            </span>
          </dt>
          <dd>{signalementEntity.raison}</dd>
          <dt>
            <span id="cible">
              <Translate contentKey="agriCycleApp.signalement.cible">Cible</Translate>
            </span>
          </dt>
          <dd>{signalementEntity.cible}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="agriCycleApp.signalement.date">Date</Translate>
            </span>
          </dt>
          <dd>{signalementEntity.date ? <TextFormat value={signalementEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.signalement.auteur">Auteur</Translate>
          </dt>
          <dd>{signalementEntity.auteur ? signalementEntity.auteur.id : ''}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.signalement.ciblePost">Cible Post</Translate>
          </dt>
          <dd>{signalementEntity.ciblePost ? signalementEntity.ciblePost.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/signalement" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/signalement/${signalementEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SignalementDetail;
