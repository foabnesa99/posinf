import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { byteSize, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRobaIliUsluga } from 'app/shared/model/roba-ili-usluga.model';
import { getEntities } from './roba-ili-usluga.reducer';

export const RobaIliUsluga = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const robaIliUslugaList = useAppSelector(state => state.robaIliUsluga.entities);
  const loading = useAppSelector(state => state.robaIliUsluga.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="roba-ili-usluga-heading" data-cy="RobaIliUslugaHeading">
        <Translate contentKey="myApp.robaIliUsluga.home.title">Roba Ili Uslugas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.robaIliUsluga.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/roba-ili-usluga/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.robaIliUsluga.home.createLabel">Create new Roba Ili Usluga</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {robaIliUslugaList && robaIliUslugaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.robaIliUsluga.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.robaIliUsluga.naziv">Naziv</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.robaIliUsluga.opis">Opis</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.robaIliUsluga.jedinicaMere">Jedinica Mere</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.robaIliUsluga.poreskaKategorija">Poreska Kategorija</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {robaIliUslugaList.map((robaIliUsluga, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/roba-ili-usluga/${robaIliUsluga.id}`} color="link" size="sm">
                      {robaIliUsluga.id}
                    </Button>
                  </td>
                  <td>{robaIliUsluga.naziv}</td>
                  <td>{robaIliUsluga.opis}</td>
                  <td>
                    {robaIliUsluga.jedinicaMere ? (
                      <Link to={`/jedinica-mere/${robaIliUsluga.jedinicaMere.id}`}>{robaIliUsluga.jedinicaMere.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {robaIliUsluga.poreskaKategorija ? (
                      <Link to={`/poreska-kategorija/${robaIliUsluga.poreskaKategorija.id}`}>{robaIliUsluga.poreskaKategorija.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/roba-ili-usluga/${robaIliUsluga.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/roba-ili-usluga/${robaIliUsluga.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/roba-ili-usluga/${robaIliUsluga.id}/delete`}
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
              <Translate contentKey="myApp.robaIliUsluga.home.notFound">No Roba Ili Uslugas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default RobaIliUsluga;
