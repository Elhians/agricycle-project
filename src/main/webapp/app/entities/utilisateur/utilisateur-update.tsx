import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { UserRole } from 'app/shared/model/enumerations/user-role.model';
import { createEntity, getEntity, reset, updateEntity } from './utilisateur.reducer';

export const UtilisateurUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const utilisateurEntity = useAppSelector(state => state.utilisateur.entity);
  const loading = useAppSelector(state => state.utilisateur.loading);
  const updating = useAppSelector(state => state.utilisateur.updating);
  const updateSuccess = useAppSelector(state => state.utilisateur.updateSuccess);
  const userRoleValues = Object.keys(UserRole);

  const handleClose = () => {
    navigate('/utilisateur');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    values.dateInscription = convertDateTimeToServer(values.dateInscription);

    const entity = {
      ...utilisateurEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          dateInscription: displayDefaultDateTime(),
        }
      : {
          role: 'AGRICULTEUR',
          ...utilisateurEntity,
          dateInscription: convertDateTimeFromServer(utilisateurEntity.dateInscription),
          user: utilisateurEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.utilisateur.home.createOrEditLabel" data-cy="UtilisateurCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.utilisateur.home.createOrEditLabel">Create or edit a Utilisateur</Translate>
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
                  id="utilisateur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.utilisateur.phone')}
                id="utilisateur-phone"
                name="phone"
                data-cy="phone"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.utilisateur.role')}
                id="utilisateur-role"
                name="role"
                data-cy="role"
                type="select"
              >
                {userRoleValues.map(userRole => (
                  <option value={userRole} key={userRole}>
                    {translate(`agriCycleApp.UserRole.${userRole}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.utilisateur.dateInscription')}
                id="utilisateur-dateInscription"
                name="dateInscription"
                data-cy="dateInscription"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="utilisateur-user"
                name="user"
                data-cy="user"
                label={translate('agriCycleApp.utilisateur.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/utilisateur" replace color="info">
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

export default UtilisateurUpdate;
