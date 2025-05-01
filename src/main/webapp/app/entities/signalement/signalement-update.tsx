import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { getEntities as getPosts } from 'app/entities/post/post.reducer';
import { Cible } from 'app/shared/model/enumerations/cible.model';
import { createEntity, getEntity, reset, updateEntity } from './signalement.reducer';

export const SignalementUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const posts = useAppSelector(state => state.post.entities);
  const signalementEntity = useAppSelector(state => state.signalement.entity);
  const loading = useAppSelector(state => state.signalement.loading);
  const updating = useAppSelector(state => state.signalement.updating);
  const updateSuccess = useAppSelector(state => state.signalement.updateSuccess);
  const cibleValues = Object.keys(Cible);

  const handleClose = () => {
    navigate('/signalement');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUtilisateurs({}));
    dispatch(getPosts({}));
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
    values.date = convertDateTimeToServer(values.date);

    const entity = {
      ...signalementEntity,
      ...values,
      auteur: utilisateurs.find(it => it.id.toString() === values.auteur?.toString()),
      ciblePost: posts.find(it => it.id.toString() === values.ciblePost?.toString()),
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
          date: displayDefaultDateTime(),
        }
      : {
          cible: 'POST',
          ...signalementEntity,
          date: convertDateTimeFromServer(signalementEntity.date),
          auteur: signalementEntity?.auteur?.id,
          ciblePost: signalementEntity?.ciblePost?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.signalement.home.createOrEditLabel" data-cy="SignalementCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.signalement.home.createOrEditLabel">Create or edit a Signalement</Translate>
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
                  id="signalement-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.signalement.raison')}
                id="signalement-raison"
                name="raison"
                data-cy="raison"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.signalement.cible')}
                id="signalement-cible"
                name="cible"
                data-cy="cible"
                type="select"
              >
                {cibleValues.map(cible => (
                  <option value={cible} key={cible}>
                    {translate(`agriCycleApp.Cible.${cible}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.signalement.date')}
                id="signalement-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="signalement-auteur"
                name="auteur"
                data-cy="auteur"
                label={translate('agriCycleApp.signalement.auteur')}
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
              <ValidatedField
                id="signalement-ciblePost"
                name="ciblePost"
                data-cy="ciblePost"
                label={translate('agriCycleApp.signalement.ciblePost')}
                type="select"
              >
                <option value="" key="0" />
                {posts
                  ? posts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/signalement" replace color="info">
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

export default SignalementUpdate;
