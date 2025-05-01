import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import QRCode from './qr-code';
import QRCodeDetail from './qr-code-detail';
import QRCodeUpdate from './qr-code-update';
import QRCodeDeleteDialog from './qr-code-delete-dialog';

const QRCodeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<QRCode />} />
    <Route path="new" element={<QRCodeUpdate />} />
    <Route path=":id">
      <Route index element={<QRCodeDetail />} />
      <Route path="edit" element={<QRCodeUpdate />} />
      <Route path="delete" element={<QRCodeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default QRCodeRoutes;
