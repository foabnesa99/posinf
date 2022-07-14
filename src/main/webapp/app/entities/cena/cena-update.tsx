import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';
import { getEntities as getRobaIliUslugas } from 'app/entities/roba-ili-usluga/roba-ili-usluga.reducer';
import { ICena } from 'app/shared/model/cena.model';
import { getEntity, updateEntity, createEntity, reset } from './cena.reducer';

export const CenaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const robaIliUslugas = useAppSelector(state => state.robaIliUsluga.entities);
  const cenaEntity = useAppSelector(state => state.cena.entity);
  const loading = useAppSelector(state => state.cena.loading);
  const updating = useAppSelector(state => state.cena.updating);
  const updateSuccess = useAppSelector(state => state.cena.updateSuccess);
  const handleClose = () => {
    props.history.push('/cena');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getRobaIliUslugas({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cenaEntity,
      ...values,
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
          ...cenaEntity,
          robaIliUsluga: cenaEntity?.robaIliUsluga?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.cena.home.createOrEditLabel" data-cy="CenaCreateUpdateHeading">
            <Translate contentKey="myApp.cena.home.createOrEditLabel">Create or edit a Cena</Translate>
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
                  id="cena-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.cena.datumVazenja')}
                id="cena-datumVazenja"
                name="datumVazenja"
                data-cy="datumVazenja"
                type="date"
              />
              <ValidatedField
                label={translate('myApp.cena.iznosCene')}
                id="cena-iznosCene"
                name="iznosCene"
                data-cy="iznosCene"
                type="text"
              />
              <ValidatedField
                id="cena-robaIliUsluga"
                name="robaIliUsluga"
                data-cy="robaIliUsluga"
                label={translate('myApp.cena.robaIliUsluga')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cena" replace color="info">
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

export default CenaUpdate;
