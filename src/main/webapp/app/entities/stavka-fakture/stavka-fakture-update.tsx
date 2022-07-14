import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';
import { getEntities as getIzlaznaFakturas } from 'app/entities/izlazna-faktura/izlazna-faktura.reducer';
import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';
import { getEntities as getRobaIliUslugas } from 'app/entities/roba-ili-usluga/roba-ili-usluga.reducer';
import { IStavkaFakture } from 'app/shared/model/stavka-fakture.model';
import { getEntity, updateEntity, createEntity, reset } from './stavka-fakture.reducer';

export const StavkaFaktureUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const izlaznaFakturas = useAppSelector(state => state.izlaznaFaktura.entities);
  const robaIliUslugas = useAppSelector(state => state.robaIliUsluga.entities);
  const stavkaFaktureEntity = useAppSelector(state => state.stavkaFakture.entity);
  const loading = useAppSelector(state => state.stavkaFakture.loading);
  const updating = useAppSelector(state => state.stavkaFakture.updating);
  const updateSuccess = useAppSelector(state => state.stavkaFakture.updateSuccess);
  const handleClose = () => {
    props.history.push('/stavka-fakture');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getIzlaznaFakturas({}));
    dispatch(getRobaIliUslugas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...stavkaFaktureEntity,
      ...values,
      izlaznaFaktura: izlaznaFakturas.find(it => it.id.toString() === values.izlaznaFaktura.toString()),
      robaIliUsluga: robaIliUslugas.find(it => it.id.toString() === values.robaIliUsluga.toString()),
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
          ...stavkaFaktureEntity,
          izlaznaFaktura: stavkaFaktureEntity?.izlaznaFaktura?.id,
          robaIliUsluga: stavkaFaktureEntity?.robaIliUsluga?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.stavkaFakture.home.createOrEditLabel" data-cy="StavkaFaktureCreateUpdateHeading">
            <Translate contentKey="myApp.stavkaFakture.home.createOrEditLabel">Create or edit a StavkaFakture</Translate>
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
                  id="stavka-fakture-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.stavkaFakture.kolicina')}
                id="stavka-fakture-kolicina"
                name="kolicina"
                data-cy="kolicina"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.stavkaFakture.rabat')}
                id="stavka-fakture-rabat"
                name="rabat"
                data-cy="rabat"
                type="text"
              />
              <ValidatedField
                id="stavka-fakture-izlaznaFaktura"
                name="izlaznaFaktura"
                data-cy="izlaznaFaktura"
                label={translate('myApp.stavkaFakture.izlaznaFaktura')}
                type="select"
              >
                <option value="" key="0" />
                {izlaznaFakturas
                  ? izlaznaFakturas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.brojFakture}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="stavka-fakture-robaIliUsluga"
                name="robaIliUsluga"
                data-cy="robaIliUsluga"
                label={translate('myApp.stavkaFakture.robaIliUsluga')}
                type="select"
              >
                <option value="" key="0" />
                {robaIliUslugas
                  ? robaIliUslugas.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.naziv}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/stavka-fakture" replace color="info">
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

export default StavkaFaktureUpdate;
