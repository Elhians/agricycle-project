import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Transporteur from './transporteur';
import TransporteurDetail from './transporteur-detail';
import TransporteurUpdate from './transporteur-update';
import TransporteurDeleteDialog from './transporteur-delete-dialog';

const TransporteurRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Transporteur />} />
    <Route path="new" element={<TransporteurUpdate />} />
    <Route path=":id">
      <Route index element={<TransporteurDetail />} />
      <Route path="edit" element={<TransporteurUpdate />} />
      <Route path="delete" element={<TransporteurDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default TransporteurRoutes;
