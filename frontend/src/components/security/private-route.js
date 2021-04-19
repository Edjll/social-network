import React from 'react';
import {Link, Redirect, Route} from 'react-router-dom';
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
                               : <Redirect to={{pathname: '/',}}/>
                       } else {
                           return <Error>
                               <div>
                                   <Link to={'/login'} className={'error__link'}>Login</Link>
                                   <span className={'error__text'}> to see this content</span>
                               </div>
                           </Error>
                       }
                   }
               }
        />
    )
}