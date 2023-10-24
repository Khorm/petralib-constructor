import React from 'react';
import { createRoot } from 'react-dom/client';
import axios from 'axios';
import { CookiesProvider } from 'react-cookie';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';

//import 'bootstrap/dist/css/bootstrap.min.css';

//import store from './redux-mod';

import App from './login-react';


export default function Root() {
  return (
    <CookiesProvider>
      <App />
    </CookiesProvider>
  );
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Root />);