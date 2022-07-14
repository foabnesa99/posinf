import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IKupac } from 'app/shared/model/kupac.model';
import { getEntity, updateEntity, createEntity, reset } from './kupac.reducer';

export const KupacUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const kupacEntity = useAppSelector(state => state.kupac.entity);
  const loading = useAppSelector(state => state.kupac.loading);
  const updating = useAppSelector(state => state.kupac.updating);
  const updateSuccess = useAppSelector(state => state.kupac.updateSuccess);
  const handleClose = () => {
    props.history.push('/kupac');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...kupacEntity,
      ...values,
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
          ...kupacEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.kupac.home.createOrEditLabel" data-cy="KupacCreateUpdateHeading">
            <Translate contentKey="myApp.kupac.home.createOrEditLabel">Create or edit a Kupac</Translate>
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
                  id="kupac-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('myApp.kupac.pib')} id="kupac-pib" name="pib" data-cy="pib" type="text" />
              <ValidatedField label={translate('myApp.kupac.mib')} id="kupac-mib" name="mib" data-cy="mib" type="text" />
              <ValidatedField label={translate('myApp.kupac.naziv')} id="kupac-naziv" name="naziv" data-cy="naziv" type="text" />
              <ValidatedField label={translate('myApp.kupac.adresa')} id="kupac-adresa" name="adresa" data-cy="adresa" type="text" />
              <ValidatedField label={translate('myApp.kupac.telefon')} id="kupac-telefon" name="telefon" data-cy="telefon" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/kupac" replace color="info">
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

export default KupacUpdate;
