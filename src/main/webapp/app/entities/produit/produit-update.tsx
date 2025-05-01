import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getUtilisateurs } from 'app/entities/utilisateur/utilisateur.reducer';
import { TypeProduit } from 'app/shared/model/enumerations/type-produit.model';
import { StatutAnnonce } from 'app/shared/model/enumerations/statut-annonce.model';
import { createEntity, getEntity, updateEntity } from './produit.reducer';

export const ProduitUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const utilisateurs = useAppSelector(state => state.utilisateur.entities);
  const produitEntity = useAppSelector(state => state.produit.entity);
  const loading = useAppSelector(state => state.produit.loading);
  const updating = useAppSelector(state => state.produit.updating);
  const updateSuccess = useAppSelector(state => state.produit.updateSuccess);
  const typeProduitValues = Object.keys(TypeProduit);
  const statutAnnonceValues = Object.keys(StatutAnnonce);

  const handleClose = () => {
    navigate('/produit');
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
    if (values.prix !== undefined && typeof values.prix !== 'number') {
      values.prix = Number(values.prix);
    }
    if (values.quantite !== undefined && typeof values.quantite !== 'number') {
      values.quantite = Number(values.quantite);
    }
    values.dateAjout = convertDateTimeToServer(values.dateAjout);

    const entity = {
      ...produitEntity,
      ...values,
      vendeur: utilisateurs.find(it => it.id.toString() === values.vendeur?.toString()),
      acheteur: utilisateurs.find(it => it.id.toString() === values.acheteur?.toString()),
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
          dateAjout: displayDefaultDateTime(),
        }
      : {
          type: 'PRODUIT',
          statut: 'OUVERT',
          ...produitEntity,
          dateAjout: convertDateTimeFromServer(produitEntity.dateAjout),
          vendeur: produitEntity?.vendeur?.id,
          acheteur: produitEntity?.acheteur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agriCycleApp.produit.home.createOrEditLabel" data-cy="ProduitCreateUpdateHeading">
            <Translate contentKey="agriCycleApp.produit.home.createOrEditLabel">Create or edit a Produit</Translate>
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
                  id="produit-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agriCycleApp.produit.nom')}
                id="produit-nom"
                name="nom"
                data-cy="nom"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.produit.description')}
                id="produit-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.produit.prix')}
                id="produit-prix"
                name="prix"
                data-cy="prix"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('agriCycleApp.produit.quantite')}
                id="produit-quantite"
                name="quantite"
                data-cy="quantite"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField label={translate('agriCycleApp.produit.type')} id="produit-type" name="type" data-cy="type" type="select">
                {typeProduitValues.map(typeProduit => (
                  <option value={typeProduit} key={typeProduit}>
                    {translate(`agriCycleApp.TypeProduit.${typeProduit}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.produit.statut')}
                id="produit-statut"
                name="statut"
                data-cy="statut"
                type="select"
              >
                {statutAnnonceValues.map(statutAnnonce => (
                  <option value={statutAnnonce} key={statutAnnonce}>
                    {translate(`agriCycleApp.StatutAnnonce.${statutAnnonce}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('agriCycleApp.produit.imageUrl')}
                id="produit-imageUrl"
                name="imageUrl"
                data-cy="imageUrl"
                type="text"
              />
              <ValidatedField
                label={translate('agriCycleApp.produit.dateAjout')}
                id="produit-dateAjout"
                name="dateAjout"
                data-cy="dateAjout"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="produit-vendeur"
                name="vendeur"
                data-cy="vendeur"
                label={translate('agriCycleApp.produit.vendeur')}
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
                id="produit-acheteur"
                name="acheteur"
                data-cy="acheteur"
                label={translate('agriCycleApp.produit.acheteur')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/produit" replace color="info">
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

export default ProduitUpdate;
