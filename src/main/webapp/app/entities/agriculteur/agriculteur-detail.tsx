import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agriculteur.reducer';

export const AgriculteurDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const agriculteurEntity = useAppSelector(state => state.agriculteur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agriculteurDetailsHeading">
          <Translate contentKey="agriCycleApp.agriculteur.detail.title">Agriculteur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agriculteurEntity.id}</dd>
          <dt>
            <span id="typeProduction">
              <Translate contentKey="agriCycleApp.agriculteur.typeProduction">Type Production</Translate>
            </span>
          </dt>
          <dd>{agriculteurEntity.typeProduction}</dd>
          <dt>
            <span id="anneeExperience">
              <Translate contentKey="agriCycleApp.agriculteur.anneeExperience">Annee Experience</Translate>
            </span>
          </dt>
          <dd>{agriculteurEntity.anneeExperience}</dd>
          <dt>
            <span id="localisation">
              <Translate contentKey="agriCycleApp.agriculteur.localisation">Localisation</Translate>
            </span>
          </dt>
          <dd>{agriculteurEntity.localisation}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.agriculteur.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{agriculteurEntity.utilisateur ? agriculteurEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/agriculteur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/agriculteur/${agriculteurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgriculteurDetail;
