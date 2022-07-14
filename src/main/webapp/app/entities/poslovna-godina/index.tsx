import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PoslovnaGodina from './poslovna-godina';
import PoslovnaGodinaDetail from './poslovna-godina-detail';
import PoslovnaGodinaUpdate from './poslovna-godina-update';
import PoslovnaGodinaDeleteDialog from './poslovna-godina-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PoslovnaGodinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PoslovnaGodinaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PoslovnaGodinaDetail} />
      <ErrorBoundaryRoute path={match.url} component={PoslovnaGodina} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PoslovnaGodinaDeleteDialog} />
  </>
);

export default Routes;
