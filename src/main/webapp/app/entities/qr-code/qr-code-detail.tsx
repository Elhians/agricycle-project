import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './qr-code.reducer';

export const QRCodeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const qRCodeEntity = useAppSelector(state => state.qRCode.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="qRCodeDetailsHeading">
          <Translate contentKey="agriCycleApp.qRCode.detail.title">QRCode</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{qRCodeEntity.id}</dd>
          <dt>
            <span id="valeur">
              <Translate contentKey="agriCycleApp.qRCode.valeur">Valeur</Translate>
            </span>
          </dt>
          <dd>{qRCodeEntity.valeur}</dd>
          <dt>
            <span id="dateExpiration">
              <Translate contentKey="agriCycleApp.qRCode.dateExpiration">Date Expiration</Translate>
            </span>
          </dt>
          <dd>
            {qRCodeEntity.dateExpiration ? <TextFormat value={qRCodeEntity.dateExpiration} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="agriCycleApp.qRCode.transaction">Transaction</Translate>
          </dt>
          <dd>{qRCodeEntity.transaction ? qRCodeEntity.transaction.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/qr-code" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/qr-code/${qRCodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QRCodeDetail;
