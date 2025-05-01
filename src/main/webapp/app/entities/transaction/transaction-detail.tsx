import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './transaction.reducer';

export const TransactionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const transactionEntity = useAppSelector(state => state.transaction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="transactionDetailsHeading">
          <Translate contentKey="agriCycleApp.transaction.detail.title">Transaction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.id}</dd>
          <dt>
            <span id="date">
              <Translate contentKey="agriCycleApp.transaction.date">Date</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.date ? <TextFormat value={transactionEntity.date} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="statut">
              <Translate contentKey="agriCycleApp.transaction.statut">Statut</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.statut}</dd>
          <dt>
            <span id="montant">
              <Translate contentKey="agriCycleApp.transaction.montant">Montant</Translate>
            </span>
          </dt>
          <dd>{transactionEntity.montant}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.transaction.produit">Produit</Translate>
          </dt>
          <dd>{transactionEntity.produit ? transactionEntity.produit.id : ''}</dd>
          <dt>
            <Translate contentKey="agriCycleApp.transaction.acheteur">Acheteur</Translate>
          </dt>
          <dd>{transactionEntity.acheteur ? transactionEntity.acheteur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/transaction" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/transaction/${transactionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TransactionDetail;
