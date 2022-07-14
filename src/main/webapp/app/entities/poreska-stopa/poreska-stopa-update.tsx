import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPoreskaKategorija } from 'app/shared/model/poreska-kategorija.model';
import { getEntities as getPoreskaKategorijas } from 'app/entities/poreska-kategorija/poreska-kategorija.reducer';
import { IPoreskaStopa } from 'app/shared/model/poreska-stopa.model';
import { getEntity, updateEntity, createEntity, reset } from './poreska-stopa.reducer';

export const PoreskaStopaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const poreskaKategorijas = useAppSelector(state => state.poreskaKategorija.entities);
  const poreskaStopaEntity = useAppSelector(state => state.poreskaStopa.entity);
  const loading = useAppSelector(state => state.poreskaStopa.loading);
  const updating = useAppSelector(state => state.poreskaStopa.updating);
  const updateSuccess = useAppSelector(state => state.poreskaStopa.updateSuccess);
  const handleClose = () => {
    props.history.push('/poreska-stopa');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPoreskaKategorijas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...poreskaStopaEntity,
      ...values,
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
          ...poreskaStopaEntity,
          poreskaKategorija: poreskaStopaEntity?.poreskaKategorija?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.poreskaStopa.home.createOrEditLabel" data-cy="PoreskaStopaCreateUpdateHeading">
            <Translate contentKey="myApp.poreskaStopa.home.createOrEditLabel">Create or edit a PoreskaStopa</Translate>
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
                  id="poreska-stopa-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.poreskaStopa.procenatPdv')}
                id="poreska-stopa-procenatPdv"
                name="procenatPdv"
                data-cy="procenatPdv"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.poreskaStopa.datumVazenja')}
                id="poreska-stopa-datumVazenja"
                name="datumVazenja"
                data-cy="datumVazenja"
                type="date"
              />
              <ValidatedField
                id="poreska-stopa-poreskaKategorija"
                name="poreskaKategorija"
                data-cy="poreskaKategorija"
                label={translate('myApp.poreskaStopa.poreskaKategorija')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poreska-stopa" replace color="info">
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

export default PoreskaStopaUpdate;
