import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IJedinicaMere } from 'app/shared/model/jedinica-mere.model';
import { getEntities as getJedinicaMeres } from 'app/entities/jedinica-mere/jedinica-mere.reducer';
import { IPoreskaKategorija } from 'app/shared/model/poreska-kategorija.model';
import { getEntities as getPoreskaKategorijas } from 'app/entities/poreska-kategorija/poreska-kategorija.reducer';
import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';
import { getEntity, updateEntity, createEntity, reset } from './roba-ili-usluga.reducer';

export const RobaIliUslugaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const jedinicaMeres = useAppSelector(state => state.jedinicaMere.entities);
  const poreskaKategorijas = useAppSelector(state => state.poreskaKategorija.entities);
  const robaIliUslugaEntity = useAppSelector(state => state.robaIliUsluga.entity);
  const loading = useAppSelector(state => state.robaIliUsluga.loading);
  const updating = useAppSelector(state => state.robaIliUsluga.updating);
  const updateSuccess = useAppSelector(state => state.robaIliUsluga.updateSuccess);
  const handleClose = () => {
    props.history.push('/roba-ili-usluga');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getJedinicaMeres({}));
    dispatch(getPoreskaKategorijas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...robaIliUslugaEntity,
      ...values,
      jedinicaMere: jedinicaMeres.find(it => it.id.toString() === values.jedinicaMere.toString()),
      poreskaKategorija: poreskaKategorijas.find(it => it.id.toString() === values.poreskaKategorija.toString()),
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
          ...robaIliUslugaEntity,
          jedinicaMere: robaIliUslugaEntity?.jedinicaMere?.id,
          poreskaKategorija: robaIliUslugaEntity?.poreskaKategorija?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.robaIliUsluga.home.createOrEditLabel" data-cy="RobaIliUslugaCreateUpdateHeading">
            <Translate contentKey="myApp.robaIliUsluga.home.createOrEditLabel">Create or edit a RobaIliUsluga</Translate>
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
                  id="roba-ili-usluga-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.robaIliUsluga.naziv')}
                id="roba-ili-usluga-naziv"
                name="naziv"
                data-cy="naziv"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.robaIliUsluga.opis')}
                id="roba-ili-usluga-opis"
                name="opis"
                data-cy="opis"
                type="textarea"
              />
              <ValidatedField
                id="roba-ili-usluga-jedinicaMere"
                name="jedinicaMere"
                data-cy="jedinicaMere"
                label={translate('myApp.robaIliUsluga.jedinicaMere')}
                type="select"
              >
                <option value="" key="0" />
                {jedinicaMeres
                  ? jedinicaMeres.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.naziv}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="roba-ili-usluga-poreskaKategorija"
                name="poreskaKategorija"
                data-cy="poreskaKategorija"
                label={translate('myApp.robaIliUsluga.poreskaKategorija')}
                type="select"
              >
                <option value="" key="0" />
                {poreskaKategorijas
                  ? poreskaKategorijas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.naziv}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/roba-ili-usluga" replace color="info">
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

export default RobaIliUslugaUpdate;
