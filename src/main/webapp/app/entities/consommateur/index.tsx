import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Consommateur from './consommateur';
import ConsommateurDetail from './consommateur-detail';
import ConsommateurUpdate from './consommateur-update';
import ConsommateurDeleteDialog from './consommateur-delete-dialog';

const ConsommateurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Consommateur />} />
    <Route path="new" element={<ConsommateurUpdate />} />
    <Route path=":id">
      <Route index element={<ConsommateurDetail />} />
      <Route path="edit" element={<ConsommateurUpdate />} />
      <Route path="delete" element={<ConsommateurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ConsommateurRoutes;
