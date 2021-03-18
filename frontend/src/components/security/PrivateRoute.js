import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import AuthService from "../../services/AuthService";


export function PrivateRoute({component: Component, roles, ...rest}) {

    return (
        <Route {...rest}
               render={
                   props => {
                       if (AuthService.authenticated()) {
                           return AuthService.hasRole(roles) ? <Component {...props}/> :
                               <Redirect to={{pathname: '/',}}/>
                       } else {
                           AuthService.login();
                       }
                   }
               }
        />
    )
}