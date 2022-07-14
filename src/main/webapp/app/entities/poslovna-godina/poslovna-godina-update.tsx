import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPreduzece } from 'app/shared/model/preduzece.model';
import { getEntities as getPreduzeces } from 'app/entities/preduzece/preduzece.reducer';
import { IPoslovnaGodina } from 'app/shared/model/poslovna-godina.model';
import { getEntity, updateEntity, createEntity, reset } from './poslovna-godina.reducer';

export const PoslovnaGodinaUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const preduzeces = useAppSelector(state => state.preduzece.entities);
  const poslovnaGodinaEntity = useAppSelector(state => state.poslovnaGodina.entity);
  const loading = useAppSelector(state => state.poslovnaGodina.loading);
  const updating = useAppSelector(state => state.poslovnaGodina.updating);
  const updateSuccess = useAppSelector(state => state.poslovnaGodina.updateSuccess);
  const handleClose = () => {
    props.history.push('/poslovna-godina');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getPreduzeces({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...poslovnaGodinaEntity,
      ...values,
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
          ...poslovnaGodinaEntity,
          preduzece: poslovnaGodinaEntity?.preduzece?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="myApp.poslovnaGodina.home.createOrEditLabel" data-cy="PoslovnaGodinaCreateUpdateHeading">
            <Translate contentKey="myApp.poslovnaGodina.home.createOrEditLabel">Create or edit a PoslovnaGodina</Translate>
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
                  id="poslovna-godina-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('myApp.poslovnaGodina.godina')}
                id="poslovna-godina-godina"
                name="godina"
                data-cy="godina"
                type="text"
              />
              <ValidatedField
                label={translate('myApp.poslovnaGodina.zakljucena')}
                id="poslovna-godina-zakljucena"
                name="zakljucena"
                data-cy="zakljucena"
                check
                type="checkbox"
              />
              <ValidatedField
                id="poslovna-godina-preduzece"
                name="preduzece"
                data-cy="preduzece"
                label={translate('myApp.poslovnaGodina.preduzece')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/poslovna-godina" replace color="info">
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

export default PoslovnaGodinaUpdate;
