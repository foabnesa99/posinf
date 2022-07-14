import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import JedinicaMere from './jedinica-mere';
import JedinicaMereDetail from './jedinica-mere-detail';
import JedinicaMereUpdate from './jedinica-mere-update';
import JedinicaMereDeleteDialog from './jedinica-mere-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={JedinicaMereUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={JedinicaMereUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={JedinicaMereDetail} />
      <ErrorBoundaryRoute path={match.url} component={JedinicaMere} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={JedinicaMereDeleteDialog} />
  </>
);

export default Routes;
