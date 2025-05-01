import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Agriculteur from './agriculteur';
import AgriculteurDetail from './agriculteur-detail';
import AgriculteurUpdate from './agriculteur-update';
import AgriculteurDeleteDialog from './agriculteur-delete-dialog';

const AgriculteurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Agriculteur />} />
    <Route path="new" element={<AgriculteurUpdate />} />
    <Route path=":id">
      <Route index element={<AgriculteurDetail />} />
      <Route path="edit" element={<AgriculteurUpdate />} />
      <Route path="delete" element={<AgriculteurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AgriculteurRoutes;
