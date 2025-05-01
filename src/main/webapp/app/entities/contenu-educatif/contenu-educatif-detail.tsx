import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './contenu-educatif.reducer';

export const ContenuEducatifDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const contenuEducatifEntity = useAppSelector(state => state.contenuEducatif.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="contenuEducatifDetailsHeading">
          <Translate contentKey="agriCycleApp.contenuEducatif.detail.title">ContenuEducatif</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{contenuEducatifEntity.id}</dd>
          <dt>
            <span id="titre">
              <Translate contentKey="agriCycleApp.contenuEducatif.titre">Titre</Translate>
            </span>
          </dt>
          <dd>{contenuEducatifEntity.titre}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="agriCycleApp.contenuEducatif.description">Description</Translate>
            </span>
          </dt>
          <dd>{contenuEducatifEntity.description}</dd>
          <dt>
            <span id="typeMedia">
              <Translate contentKey="agriCycleApp.contenuEducatif.typeMedia">Type Media</Translate>
            </span>
          </dt>
          <dd>{contenuEducatifEntity.typeMedia}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="agriCycleApp.contenuEducatif.url">Url</Translate>
            </span>
          </dt>
          <dd>{contenuEducatifEntity.url}</dd>
          <dt>
            <span id="datePublication">
              <Translate contentKey="agriCycleApp.contenuEducatif.datePublication">Date Publication</Translate>
            </span>
          </dt>
          <dd>
            {contenuEducatifEntity.datePublication ? (
              <TextFormat value={contenuEducatifEntity.datePublication} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="agriCycleApp.contenuEducatif.auteur">Auteur</Translate>
          </dt>
          <dd>{contenuEducatifEntity.auteur ? contenuEducatifEntity.auteur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/contenu-educatif" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/contenu-educatif/${contenuEducatifEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ContenuEducatifDetail;
