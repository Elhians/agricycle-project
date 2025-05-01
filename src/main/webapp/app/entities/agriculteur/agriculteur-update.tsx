import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { createEntity, getEntity, reset, updateEntity } from './agriculteur.reducer';

export const AgriculteurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const agriculteurEntity = useAppSelector(state => state.agriculteur.entity);
  const loading = useAppSelector(state => state.agriculteur.loading);
  const updating = useAppSelector(state => state.agriculteur.updating);
  const updateSuccess = useAppSelector(state => state.agriculteur.updateSuccess);

  const handleClose = () => {
    navigate('/agriculteur');
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
    if (values.anneeExperience !== undefined && typeof values.anneeExperience !== 'number') {
      values.anneeExperience = Number(values.anneeExperience);
    }

    const entity = {
      ...agriculteurEntity,
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
          ...agriculteurEntity,
          utilisateur: agriculteurEntity?.utilisateur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.agriculteur.home.createOrEditLabel" data-cy="AgriculteurCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.agriculteur.home.createOrEditLabel">Create or edit a Agriculteur</Translate>
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
                  id="agriculteur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.agriculteur.typeProduction')}
                id="agriculteur-typeProduction"
                name="typeProduction"
                data-cy="typeProduction"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.agriculteur.anneeExperience')}
                id="agriculteur-anneeExperience"
                name="anneeExperience"
                data-cy="anneeExperience"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.agriculteur.localisation')}
                id="agriculteur-localisation"
                name="localisation"
                data-cy="localisation"
                type="text"
              />
              <ValidatedField
                id="agriculteur-utilisateur"
                name="utilisateur"
                data-cy="utilisateur"
                label={translate('agriCycleApp.agriculteur.utilisateur')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/agriculteur" replace color="info">
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

export default AgriculteurUpdate;
