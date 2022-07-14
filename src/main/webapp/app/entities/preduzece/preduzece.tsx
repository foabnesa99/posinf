import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPreduzece } from 'app/shared/model/preduzece.model';
import { getEntities } from './preduzece.reducer';

export const Preduzece = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const preduzeceList = useAppSelector(state => state.preduzece.entities);
  const loading = useAppSelector(state => state.preduzece.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="preduzece-heading" data-cy="PreduzeceHeading">
        <Translate contentKey="myApp.preduzece.home.title">Preduzeces</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="myApp.preduzece.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/preduzece/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="myApp.preduzece.home.createLabel">Create new Preduzece</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {preduzeceList && preduzeceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="myApp.preduzece.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.preduzece.pib">Pib</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.preduzece.naziv">Naziv</Translate>
                </th>
                <th>
                  <Translate contentKey="myApp.preduzece.adresa">Adresa</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {preduzeceList.map((preduzece, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/preduzece/${preduzece.id}`} color="link" size="sm">
                      {preduzece.id}
                    </Button>
                  </td>
                  <td>{preduzece.pib}</td>
                  <td>{preduzece.naziv}</td>
                  <td>{preduzece.adresa}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/preduzece/${preduzece.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/preduzece/${preduzece.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/preduzece/${preduzece.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="myApp.preduzece.home.notFound">No Preduzeces found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Preduzece;
