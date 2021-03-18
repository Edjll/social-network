import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import AuthService from "./services/AuthService";
import RequestService from "./services/RequestService";

const app = () => ReactDOM.render(
    <React.StrictMode>
        <App/>
    </React.StrictMode>,
    document.getElementById('root')
);

AuthService.initKeycloak(app);
RequestService.configure();