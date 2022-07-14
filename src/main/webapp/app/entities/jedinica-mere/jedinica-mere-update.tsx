import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IJedinicaMere } from 'app/shared/model/jedinica-mere.model';
import { getEntity, updateEntity, createEntity, reset } from './jedinica-mere.reducer';

export const JedinicaMereUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const jedinicaMereEntity = useAppSelector(state => state.jedinicaMere.entity);
  const loading = useAppSelector(state => state.jedinicaMere.loading);
  const updating = useAppSelector(state => state.jedinicaMere.updating);
  const updateSuccess = useAppSelector(state => state.jedinicaMere.updateSuccess);
  const handleClose = () => {
    props.history.push('/jedinica-mere');
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
      ...jedinicaMereEntity,
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
          ...jedinicaMereEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.jedinicaMere.home.createOrEditLabel" data-cy="JedinicaMereCreateUpdateHeading">
            <Translate contentKey="myApp.jedinicaMere.home.createOrEditLabel">Create or edit a JedinicaMere</Translate>
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
                  id="jedinica-mere-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.jedinicaMere.naziv')}
                id="jedinica-mere-naziv"
                name="naziv"
                data-cy="naziv"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.jedinicaMere.skraceniNaziv')}
                id="jedinica-mere-skraceniNaziv"
                name="skraceniNaziv"
                data-cy="skraceniNaziv"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/jedinica-mere" replace color="info">
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

export default JedinicaMereUpdate;
