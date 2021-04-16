import * as React from "react";
import {UserCard} from "./user-card";
import AuthService from "../../../services/AuthService";

export const UserSubscriberCard = (props) => {

    return (
        <UserCard predicate={() => AuthService.isAuthenticated() && AuthService.getId() === props.userId} info={props.info}/>
    );
}