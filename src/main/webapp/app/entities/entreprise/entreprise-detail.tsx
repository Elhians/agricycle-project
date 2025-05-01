import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './entreprise.reducer';

export const EntrepriseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const entrepriseEntity = useAppSelector(state => state.entreprise.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="entrepriseDetailsHeading">
          <Translate contentKey="agriCycleApp.entreprise.detail.title">Entreprise</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{entrepriseEntity.id}</dd>
          <dt>
            <span id="nomEntreprise">
              <Translate contentKey="agriCycleApp.entreprise.nomEntreprise">Nom Entreprise</Translate>
            </span>
          </dt>
          <dd>{entrepriseEntity.nomEntreprise}</dd>
          <dt>
            <span id="typeActivite">
              <Translate contentKey="agriCycleApp.entreprise.typeActivite">Type Activite</Translate>
            </span>
          </dt>
          <dd>{entrepriseEntity.typeActivite}</dd>
          <dt>
            <span id="licence">
              <Translate contentKey="agriCycleApp.entreprise.licence">Licence</Translate>
            </span>
          </dt>
          <dd>{entrepriseEntity.licence}</dd>
          <dt>
            <span id="adressePhysique">
              <Translate contentKey="agriCycleApp.entreprise.adressePhysique">Adresse Physique</Translate>
            </span>
          </dt>
          <dd>{entrepriseEntity.adressePhysique}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.entreprise.utilisateur">Utilisateur</Translate>
          </dt>
          <dd>{entrepriseEntity.utilisateur ? entrepriseEntity.utilisateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/entreprise" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/entreprise/${entrepriseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EntrepriseDetail;
