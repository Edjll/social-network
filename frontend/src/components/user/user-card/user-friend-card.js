import * as React from "react";
import {UserCard} from "./user-card";
import AuthService from "../../../services/AuthService";

export const UserFriendCard = (props) => {

    if (AuthService.isAuthenticated() && props.userId === AuthService.getId())
        return (<UserCard info={{...props.info, friendId: props.userId}}/>);
    else
        return (<UserCard info={{...props.info, friendId: props.userId}}> </UserCard>);
}