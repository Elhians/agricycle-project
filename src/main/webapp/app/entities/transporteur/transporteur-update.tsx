import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { TypeVehicule } from 'app/shared/model/enumerations/type-vehicule.model';
import { createEntity, getEntity, reset, updateEntity } from './transporteur.reducer';

export const TransporteurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const transporteurEntity = useAppSelector(state => state.transporteur.entity);
  const loading = useAppSelector(state => state.transporteur.loading);
  const updating = useAppSelector(state => state.transporteur.updating);
  const updateSuccess = useAppSelector(state => state.transporteur.updateSuccess);
  const typeVehiculeValues = Object.keys(TypeVehicule);

  const handleClose = () => {
    navigate('/transporteur');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUtilisateurs({}));
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
    if (values.capacite !== undefined && typeof values.capacite !== 'number') {
      values.capacite = Number(values.capacite);
    }

    const entity = {
      ...transporteurEntity,
      ...values,
      utilisateur: utilisateurs.find(it => it.id.toString() === values.utilisateur?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          typeVehicule: 'VOITURE',
          ...transporteurEntity,
          utilisateur: transporteurEntity?.utilisateur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.transporteur.home.createOrEditLabel" data-cy="TransporteurCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.transporteur.home.createOrEditLabel">Create or edit a Transporteur</Translate>
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
                  id="transporteur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.transporteur.zoneCouverture')}
                id="transporteur-zoneCouverture"
                name="zoneCouverture"
                data-cy="zoneCouverture"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.transporteur.typeVehicule')}
                id="transporteur-typeVehicule"
                name="typeVehicule"
                data-cy="typeVehicule"
                type="select"
              >
                {typeVehiculeValues.map(typeVehicule => (
                  <option value={typeVehicule} key={typeVehicule}>
                    {translate(`agriCycleApp.TypeVehicule.${typeVehicule}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.transporteur.capacite')}
                id="transporteur-capacite"
                name="capacite"
                data-cy="capacite"
                type="text"
              />
              <ValidatedField
                id="transporteur-utilisateur"
                name="utilisateur"
                data-cy="utilisateur"
                label={translate('agriCycleApp.transporteur.utilisateur')}
                type="select"
              >
                <option value="" key="0" />
                {utilisateurs
                  ? utilisateurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/transporteur" replace color="info">
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

export default TransporteurUpdate;
