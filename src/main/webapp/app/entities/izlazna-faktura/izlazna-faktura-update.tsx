import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IKupac } from 'app/shared/model/kupac.model';
import { getEntities as getKupacs } from 'app/entities/kupac/kupac.reducer';
import { IPreduzece } from 'app/shared/model/preduzece.model';
import { getEntities as getPreduzeces } from 'app/entities/preduzece/preduzece.reducer';
import { IIzlaznaFaktura } from 'app/shared/model/izlazna-faktura.model';
import { StatusFakture } from 'app/shared/model/enumerations/status-fakture.model';
import { getEntity, updateEntity, createEntity, reset } from './izlazna-faktura.reducer';

export const IzlaznaFakturaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const kupacs = useAppSelector(state => state.kupac.entities);
  const preduzeces = useAppSelector(state => state.preduzece.entities);
  const izlaznaFakturaEntity = useAppSelector(state => state.izlaznaFaktura.entity);
  const loading = useAppSelector(state => state.izlaznaFaktura.loading);
  const updating = useAppSelector(state => state.izlaznaFaktura.updating);
  const updateSuccess = useAppSelector(state => state.izlaznaFaktura.updateSuccess);
  const statusFaktureValues = Object.keys(StatusFakture);
  const handleClose = () => {
    props.history.push('/izlazna-faktura');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getKupacs({}));
    dispatch(getPreduzeces({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...izlaznaFakturaEntity,
      ...values,
      kupac: kupacs.find(it => it.id.toString() === values.kupac.toString()),
      preduzece: preduzeces.find(it => it.id.toString() === values.preduzece.toString()),
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
          status: 'FORMIRANJE',
          ...izlaznaFakturaEntity,
          kupac: izlaznaFakturaEntity?.kupac?.id,
          preduzece: izlaznaFakturaEntity?.preduzece?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.izlaznaFaktura.home.createOrEditLabel" data-cy="IzlaznaFakturaCreateUpdateHeading">
            <Translate contentKey="myApp.izlaznaFaktura.home.createOrEditLabel">Create or edit a IzlaznaFaktura</Translate>
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
                  id="izlazna-faktura-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.brojFakture')}
                id="izlazna-faktura-brojFakture"
                name="brojFakture"
                data-cy="brojFakture"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.datumIzdavanja')}
                id="izlazna-faktura-datumIzdavanja"
                name="datumIzdavanja"
                data-cy="datumIzdavanja"
                type="date"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.datumValute')}
                id="izlazna-faktura-datumValute"
                name="datumValute"
                data-cy="datumValute"
                type="date"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.ukupnaOsnovica')}
                id="izlazna-faktura-ukupnaOsnovica"
                name="ukupnaOsnovica"
                data-cy="ukupnaOsnovica"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.ukupanRabat')}
                id="izlazna-faktura-ukupanRabat"
                name="ukupanRabat"
                data-cy="ukupanRabat"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.ukupanIznos')}
                id="izlazna-faktura-ukupanIznos"
                name="ukupanIznos"
                data-cy="ukupanIznos"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.izlaznaFaktura.status')}
                id="izlazna-faktura-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {statusFaktureValues.map(statusFakture => (
                  <option value={statusFakture} key={statusFakture}>
                    {translate('myApp.StatusFakture.' + statusFakture)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="izlazna-faktura-kupac"
                name="kupac"
                data-cy="kupac"
                label={translate('myApp.izlaznaFaktura.kupac')}
                type="select"
              >
                <option value="" key="0" />
                {kupacs
                  ? kupacs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="izlazna-faktura-preduzece"
                name="preduzece"
                data-cy="preduzece"
                label={translate('myApp.izlaznaFaktura.preduzece')}
                type="select"
              >
                <option value="" key="0" />
                {preduzeces
                  ? preduzeces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/izlazna-faktura" replace color="info">
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

export default IzlaznaFakturaUpdate;
