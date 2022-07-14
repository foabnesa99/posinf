import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './poreska-stopa.reducer';

export const PoreskaStopaDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const poreskaStopaEntity = useAppSelector(state => state.poreskaStopa.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="poreskaStopaDetailsHeading">
          <Translate contentKey="myApp.poreskaStopa.detail.title">PoreskaStopa</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{poreskaStopaEntity.id}</dd>
          <dt>
            <span id="procenatPdv">
              <Translate contentKey="myApp.poreskaStopa.procenatPdv">Procenat Pdv</Translate>
            </span>
          </dt>
          <dd>{poreskaStopaEntity.procenatPdv}</dd>
          <dt>
            <span id="datumVazenja">
              <Translate contentKey="myApp.poreskaStopa.datumVazenja">Datum Vazenja</Translate>
            </span>
          </dt>
          <dd>
            {poreskaStopaEntity.datumVazenja ? (
              <TextFormat value={poreskaStopaEntity.datumVazenja} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="myApp.poreskaStopa.poreskaKategorija">Poreska Kategorija</Translate>
          </dt>
          <dd>{poreskaStopaEntity.poreskaKategorija ? poreskaStopaEntity.poreskaKategorija.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/poreska-stopa" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/poreska-stopa/${poreskaStopaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PoreskaStopaDetail;
