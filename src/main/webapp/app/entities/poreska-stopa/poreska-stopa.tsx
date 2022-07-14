import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPoreskaStopa } from 'app/shared/model/poreska-stopa.model';
import { getEntities } from './poreska-stopa.reducer';

export const PoreskaStopa = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const poreskaStopaList = useAppSelector(state => state.poreskaStopa.entities);
  const loading = useAppSelector(state => state.poreskaStopa.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="poreska-stopa-heading" data-cy="PoreskaStopaHeading">
        <Translate contentKey="myApp.poreskaStopa.home.title">Poreska Stopas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.poreskaStopa.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/poreska-stopa/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.poreskaStopa.home.createLabel">Create new Poreska Stopa</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {poreskaStopaList && poreskaStopaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.poreskaStopa.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poreskaStopa.procenatPdv">Procenat Pdv</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poreskaStopa.datumVazenja">Datum Vazenja</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.poreskaStopa.poreskaKategorija">Poreska Kategorija</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {poreskaStopaList.map((poreskaStopa, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/poreska-stopa/${poreskaStopa.id}`} color="link" size="sm">
                      {poreskaStopa.id}
                    </Button>
                  </td>
                  <td>{poreskaStopa.procenatPdv}</td>
                  <td>
                    {poreskaStopa.datumVazenja ? (
                      <TextFormat type="date" value={poreskaStopa.datumVazenja} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {poreskaStopa.poreskaKategorija ? (
                      <Link to={`/poreska-kategorija/${poreskaStopa.poreskaKategorija.id}`}>{poreskaStopa.poreskaKategorija.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/poreska-stopa/${poreskaStopa.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/poreska-stopa/${poreskaStopa.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/poreska-stopa/${poreskaStopa.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="myApp.poreskaStopa.home.notFound">No Poreska Stopas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default PoreskaStopa;
