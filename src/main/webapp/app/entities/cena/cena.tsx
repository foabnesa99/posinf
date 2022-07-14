import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICena } from 'app/shared/model/cena.model';
import { getEntities } from './cena.reducer';

export const Cena = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const cenaList = useAppSelector(state => state.cena.entities);
  const loading = useAppSelector(state => state.cena.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="cena-heading" data-cy="CenaHeading">
        <Translate contentKey="myApp.cena.home.title">Cenas</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> <Translate contentKey="myApp.cena.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cena/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.cena.home.createLabel">Create new Cena</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cenaList && cenaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.cena.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.cena.datumVazenja">Datum Vazenja</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.cena.iznosCene">Iznos Cene</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.cena.robaIliUsluga">Roba Ili Usluga</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cenaList.map((cena, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/cena/${cena.id}`} color="link" size="sm">
                      {cena.id}
                    </Button>
                  </td>
                  <td>{cena.datumVazenja ? <TextFormat type="date" value={cena.datumVazenja} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{cena.iznosCene}</td>
                  <td>{cena.robaIliUsluga ? <Link to={`/roba-ili-usluga/${cena.robaIliUsluga.id}`}>{cena.robaIliUsluga.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/cena/${cena.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cena/${cena.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/cena/${cena.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="myApp.cena.home.notFound">No Cenas found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Cena;
