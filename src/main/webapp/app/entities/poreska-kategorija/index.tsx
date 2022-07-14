import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import PoreskaKategorija from './poreska-kategorija';
import PoreskaKategorijaDetail from './poreska-kategorija-detail';
import PoreskaKategorijaUpdate from './poreska-kategorija-update';
import PoreskaKategorijaDeleteDialog from './poreska-kategorija-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PoreskaKategorijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PoreskaKategorijaUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PoreskaKategorijaDetail} />
      <ErrorBoundaryRoute path={match.url} component={PoreskaKategorija} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PoreskaKategorijaDeleteDialog} />
  </>
);

export default Routes;
