import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Preduzece from './preduzece';
import PreduzeceDetail from './preduzece-detail';
import PreduzeceUpdate from './preduzece-update';
import PreduzeceDeleteDialog from './preduzece-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PreduzeceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PreduzeceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PreduzeceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Preduzece} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PreduzeceDeleteDialog} />
  </>
);

export default Routes;
