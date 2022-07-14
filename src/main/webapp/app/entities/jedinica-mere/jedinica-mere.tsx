import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IJedinicaMere } from 'app/shared/model/jedinica-mere.model';
import { getEntities } from './jedinica-mere.reducer';

export const JedinicaMere = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const jedinicaMereList = useAppSelector(state => state.jedinicaMere.entities);
  const loading = useAppSelector(state => state.jedinicaMere.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="jedinica-mere-heading" data-cy="JedinicaMereHeading">
        <Translate contentKey="myApp.jedinicaMere.home.title">Jedinica Meres</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.jedinicaMere.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/jedinica-mere/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.jedinicaMere.home.createLabel">Create new Jedinica Mere</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {jedinicaMereList && jedinicaMereList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.jedinicaMere.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.jedinicaMere.naziv">Naziv</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.jedinicaMere.skraceniNaziv">Skraceni Naziv</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {jedinicaMereList.map((jedinicaMere, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/jedinica-mere/${jedinicaMere.id}`} color="link" size="sm">
                      {jedinicaMere.id}
                    </Button>
                  </td>
                  <td>{jedinicaMere.naziv}</td>
                  <td>{jedinicaMere.skraceniNaziv}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/jedinica-mere/${jedinicaMere.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/jedinica-mere/${jedinicaMere.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/jedinica-mere/${jedinicaMere.id}/delete`}
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
              <Translate contentKey="myApp.jedinicaMere.home.notFound">No Jedinica Meres found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default JedinicaMere;
