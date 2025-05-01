import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Commercant from './commercant';
import CommercantDetail from './commercant-detail';
import CommercantUpdate from './commercant-update';
import CommercantDeleteDialog from './commercant-delete-dialog';

const CommercantRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Commercant />} />
    <Route path="new" element={<CommercantUpdate />} />
    <Route path=":id">
      <Route index element={<CommercantDetail />} />
      <Route path="edit" element={<CommercantUpdate />} />
      <Route path="delete" element={<CommercantDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CommercantRoutes;
