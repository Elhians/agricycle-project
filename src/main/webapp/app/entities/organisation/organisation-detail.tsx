import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './organisation.reducer';

export const OrganisationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const organisationEntity = useAppSelector(state => state.organisation.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="organisationDetailsHeading">
          <Translate contentKey="agriCycleApp.organisation.detail.title">Organisation</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{organisationEntity.id}</dd>
          <dt>
            <span id="nomOrganisation">
              <Translate contentKey="agriCycleApp.organisation.nomOrganisation">Nom Organisation</Translate>
            </span>
          </dt>
          <dd>{organisationEntity.nomOrganisation}</dd>
          <dt>
            <span id="siteWeb">
              <Translate contentKey="agriCycleApp.organisation.siteWeb">Site Web</Translate>
            </span>
          </dt>
          <dd>{organisationEntity.siteWeb}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.organisation.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{organisationEntity.utilisateur ? organisationEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/organisation" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/organisation/${organisationEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrganisationDetail;
