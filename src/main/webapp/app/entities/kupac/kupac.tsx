import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IKupac } from 'app/shared/model/kupac.model';
import { getEntities } from './kupac.reducer';

export const Kupac = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const kupacList = useAppSelector(state => state.kupac.entities);
  const loading = useAppSelector(state => state.kupac.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="kupac-heading" data-cy="KupacHeading">
        <Translate contentKey="myApp.kupac.home.title">Kupacs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.kupac.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/kupac/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.kupac.home.createLabel">Create new Kupac</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {kupacList && kupacList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.kupac.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.kupac.pib">Pib</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.kupac.mib">Mib</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.kupac.naziv">Naziv</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.kupac.adresa">Adresa</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.kupac.telefon">Telefon</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {kupacList.map((kupac, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/kupac/${kupac.id}`} color="link" size="sm">
                      {kupac.id}
                    </Button>
                  </td>
                  <td>{kupac.pib}</td>
                  <td>{kupac.mib}</td>
                  <td>{kupac.naziv}</td>
                  <td>{kupac.adresa}</td>
                  <td>{kupac.telefon}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/kupac/${kupac.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/kupac/${kupac.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/kupac/${kupac.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="myApp.kupac.home.notFound">No Kupacs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Kupac;
