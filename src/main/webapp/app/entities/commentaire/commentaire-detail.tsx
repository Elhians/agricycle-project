import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './commentaire.reducer';

export const CommentaireDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const commentaireEntity = useAppSelector(state => state.commentaire.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="commentaireDetailsHeading">
          <Translate contentKey="agriCycleApp.commentaire.detail.title">Commentaire</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{commentaireEntity.id}</dd>
          <dt>
            <span id="contenu">
              <Translate contentKey="agriCycleApp.commentaire.contenu">Contenu</Translate>
            </span>
          </dt>
          <dd>{commentaireEntity.contenu}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="agriCycleApp.commentaire.date">Date</Translate>
            </span>
          </dt>
          <dd>{commentaireEntity.date ? <TextFormat value={commentaireEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.commentaire.post">Post</Translate>
          </dt>
          <dd>{commentaireEntity.post ? commentaireEntity.post.id : ''}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.commentaire.auteur">Auteur</Translate>
          </dt>
          <dd>{commentaireEntity.auteur ? commentaireEntity.auteur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/commentaire" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/commentaire/${commentaireEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CommentaireDetail;
