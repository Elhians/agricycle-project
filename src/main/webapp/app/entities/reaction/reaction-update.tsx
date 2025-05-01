import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { getEntities as getPosts } from 'app/entities/post/post.reducer';
import { TypeReaction } from 'app/shared/model/enumerations/type-reaction.model';
import { Cible } from 'app/shared/model/enumerations/cible.model';
import { createEntity, getEntity, reset, updateEntity } from './reaction.reducer';

export const ReactionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const posts = useAppSelector(state => state.post.entities);
  const reactionEntity = useAppSelector(state => state.reaction.entity);
  const loading = useAppSelector(state => state.reaction.loading);
  const updating = useAppSelector(state => state.reaction.updating);
  const updateSuccess = useAppSelector(state => state.reaction.updateSuccess);
  const typeReactionValues = Object.keys(TypeReaction);
  const cibleValues = Object.keys(Cible);

  const handleClose = () => {
    navigate('/reaction');
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
      ...reactionEntity,
      ...values,
      utilisateur: utilisateurs.find(it => it.id.toString() === values.utilisateur?.toString()),
      post: posts.find(it => it.id.toString() === values.post?.toString()),
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
          type: 'LIKE',
          cible: 'POST',
          ...reactionEntity,
          date: convertDateTimeFromServer(reactionEntity.date),
          utilisateur: reactionEntity?.utilisateur?.id,
          post: reactionEntity?.post?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.reaction.home.createOrEditLabel" data-cy="ReactionCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.reaction.home.createOrEditLabel">Create or edit a Reaction</Translate>
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
                  id="reaction-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('agriCycleApp.reaction.type')} id="reaction-type" name="type" data-cy="type" type="select">
                {typeReactionValues.map(typeReaction => (
                  <option value={typeReaction} key={typeReaction}>
                    {translate(`agriCycleApp.TypeReaction.${typeReaction}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.reaction.date')}
                id="reaction-date"
                name="date"
                data-cy="date"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('agriCycleApp.reaction.cible')}
                id="reaction-cible"
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
                id="reaction-utilisateur"
                name="utilisateur"
                data-cy="utilisateur"
                label={translate('agriCycleApp.reaction.utilisateur')}
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
              <ValidatedField id="reaction-post" name="post" data-cy="post" label={translate('agriCycleApp.reaction.post')} type="select">
                <option value="" key="0" />
                {posts
                  ? posts.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/reaction" replace color="info">
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

export default ReactionUpdate;
