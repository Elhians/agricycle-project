import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getTransactions } from 'app/entities/transaction/transaction.reducer';
import { createEntity, getEntity, reset, updateEntity } from './qr-code.reducer';

export const QRCodeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const transactions = useAppSelector(state => state.transaction.entities);
  const qRCodeEntity = useAppSelector(state => state.qRCode.entity);
  const loading = useAppSelector(state => state.qRCode.loading);
  const updating = useAppSelector(state => state.qRCode.updating);
  const updateSuccess = useAppSelector(state => state.qRCode.updateSuccess);

  const handleClose = () => {
    navigate('/qr-code');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getTransactions({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.dateExpiration = convertDateTimeToServer(values.dateExpiration);

    const entity = {
      ...qRCodeEntity,
      ...values,
      transaction: transactions.find(it => it.id.toString() === values.transaction?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateExpiration: displayDefaultDateTime(),
        }
      : {
          ...qRCodeEntity,
          dateExpiration: convertDateTimeFromServer(qRCodeEntity.dateExpiration),
          transaction: qRCodeEntity?.transaction?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.qRCode.home.createOrEditLabel" data-cy="QRCodeCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.qRCode.home.createOrEditLabel">Create or edit a QRCode</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="qr-code-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.qRCode.valeur')}
                id="qr-code-valeur"
                name="valeur"
                data-cy="valeur"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.qRCode.dateExpiration')}
                id="qr-code-dateExpiration"
                name="dateExpiration"
                data-cy="dateExpiration"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="qr-code-transaction"
                name="transaction"
                data-cy="transaction"
                label={translate('agriCycleApp.qRCode.transaction')}
                type="select"
              >
                <option value="" key="0" />
                {transactions
                  ? transactions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/qr-code" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default QRCodeUpdate;
