import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PoreskaStopa from './poreska-stopa';
import PoreskaStopaDetail from './poreska-stopa-detail';
import PoreskaStopaUpdate from './poreska-stopa-update';
import PoreskaStopaDeleteDialog from './poreska-stopa-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PoreskaStopaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PoreskaStopaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PoreskaStopaDetail} />
      <ErrorBoundaryRoute path={match.url} component={PoreskaStopa} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PoreskaStopaDeleteDialog} />
  </>
);

export default Routes;
