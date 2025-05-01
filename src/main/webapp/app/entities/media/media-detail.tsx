import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './media.reducer';

export const MediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mediaEntity = useAppSelector(state => state.media.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mediaDetailsHeading">
          <Translate contentKey="agriCycleApp.media.detail.title">Media</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="agriCycleApp.media.url">Url</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.url}</dd>
          <dt>
            <span id="typeMedia">
              <Translate contentKey="agriCycleApp.media.typeMedia">Type Media</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.typeMedia}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.media.post">Post</Translate>
          </dt>
          <dd>{mediaEntity.post ? mediaEntity.post.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/media/${mediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MediaDetail;
