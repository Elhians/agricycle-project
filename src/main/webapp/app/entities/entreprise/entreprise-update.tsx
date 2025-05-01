import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { createEntity, getEntity, reset, updateEntity } from './entreprise.reducer';

export const EntrepriseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const entrepriseEntity = useAppSelector(state => state.entreprise.entity);
  const loading = useAppSelector(state => state.entreprise.loading);
  const updating = useAppSelector(state => state.entreprise.updating);
  const updateSuccess = useAppSelector(state => state.entreprise.updateSuccess);

  const handleClose = () => {
    navigate('/entreprise');
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

    const entity = {
      ...entrepriseEntity,
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
          ...entrepriseEntity,
          utilisateur: entrepriseEntity?.utilisateur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.entreprise.home.createOrEditLabel" data-cy="EntrepriseCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.entreprise.home.createOrEditLabel">Create or edit a Entreprise</Translate>
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
                  id="entreprise-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.entreprise.nomEntreprise')}
                id="entreprise-nomEntreprise"
                name="nomEntreprise"
                data-cy="nomEntreprise"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.entreprise.typeActivite')}
                id="entreprise-typeActivite"
                name="typeActivite"
                data-cy="typeActivite"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.entreprise.licence')}
                id="entreprise-licence"
                name="licence"
                data-cy="licence"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.entreprise.adressePhysique')}
                id="entreprise-adressePhysique"
                name="adressePhysique"
                data-cy="adressePhysique"
                type="text"
              />
              <ValidatedField
                id="entreprise-utilisateur"
                name="utilisateur"
                data-cy="utilisateur"
                label={translate('agriCycleApp.entreprise.utilisateur')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/entreprise" replace color="info">
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

export default EntrepriseUpdate;
