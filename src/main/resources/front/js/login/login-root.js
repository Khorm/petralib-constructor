import React from 'react';
import { createRoot } from 'react-dom/client';

import axios from 'axios';
import { CookiesProvider } from 'react-cookie';

import Login from './login-react';

import 'bootstrap/dist/css/bootstrap.min.css';


export default function Root() {
  return (
    <CookiesProvider defaultSetOptions={{ path: '/' }}>
      <Login />
    </CookiesProvider>
  );
}

const container = document.getElementById('react');
const root = createRoot(container);
root.render(<Root />);