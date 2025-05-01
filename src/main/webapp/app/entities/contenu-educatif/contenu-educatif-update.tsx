import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { TypeMedia } from 'app/shared/model/enumerations/type-media.model';
import { createEntity, getEntity, updateEntity } from './contenu-educatif.reducer';

export const ContenuEducatifUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const contenuEducatifEntity = useAppSelector(state => state.contenuEducatif.entity);
  const loading = useAppSelector(state => state.contenuEducatif.loading);
  const updating = useAppSelector(state => state.contenuEducatif.updating);
  const updateSuccess = useAppSelector(state => state.contenuEducatif.updateSuccess);
  const typeMediaValues = Object.keys(TypeMedia);

  const handleClose = () => {
    navigate('/contenu-educatif');
  };

  useEffect(() => {
    if (!isNew) {
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
    values.datePublication = convertDateTimeToServer(values.datePublication);

    const entity = {
      ...contenuEducatifEntity,
      ...values,
      auteur: utilisateurs.find(it => it.id.toString() === values.auteur?.toString()),
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
          datePublication: displayDefaultDateTime(),
        }
      : {
          typeMedia: 'TEXTE',
          ...contenuEducatifEntity,
          datePublication: convertDateTimeFromServer(contenuEducatifEntity.datePublication),
          auteur: contenuEducatifEntity?.auteur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.contenuEducatif.home.createOrEditLabel" data-cy="ContenuEducatifCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.contenuEducatif.home.createOrEditLabel">Create or edit a ContenuEducatif</Translate>
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
                  id="contenu-educatif-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.contenuEducatif.titre')}
                id="contenu-educatif-titre"
                name="titre"
                data-cy="titre"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.contenuEducatif.description')}
                id="contenu-educatif-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.contenuEducatif.typeMedia')}
                id="contenu-educatif-typeMedia"
                name="typeMedia"
                data-cy="typeMedia"
                type="select"
              >
                {typeMediaValues.map(typeMedia => (
                  <option value={typeMedia} key={typeMedia}>
                    {translate(`agriCycleApp.TypeMedia.${typeMedia}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.contenuEducatif.url')}
                id="contenu-educatif-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.contenuEducatif.datePublication')}
                id="contenu-educatif-datePublication"
                name="datePublication"
                data-cy="datePublication"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="contenu-educatif-auteur"
                name="auteur"
                data-cy="auteur"
                label={translate('agriCycleApp.contenuEducatif.auteur')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/contenu-educatif" replace color="info">
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

export default ContenuEducatifUpdate;
