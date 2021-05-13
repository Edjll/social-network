import React from 'react';
import {Redirect, Route} from 'react-router-dom';
import AuthService from "../../services/AuthService";
import {Error} from "../error/error";

export function PrivateRoute({component: Component, roles, ...rest}) {

    return (
        <Route {...rest}
               render={
                   props => {
                       if (AuthService.isAuthenticated()) {
                           return AuthService.hasRole(roles)
                               ? <Component {...props}/>
                               : <Error>
                                   <div>
                                       <span className={'error__text'}>You don't have the right roles</span>
                                   </div>
                               </Error>
                       } else {
                           return <Redirect to={{pathname: '/login',}}/>
                       }
                   }
               }
        />
    )
}