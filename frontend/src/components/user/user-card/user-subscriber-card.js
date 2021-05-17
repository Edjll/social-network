import * as React from "react";
import {UserCard} from "./user-card";
import AuthService from "../../../services/AuthService";
import UserFriendStatus from "../../../services/UserFriendStatus";
import {FormButton} from "../../form/form-button";

export const UserSubscriberCard = (props) => {

    if (AuthService.isAuthenticated() && props.userId === AuthService.getId())
        return (<UserCard info={{...props.info, friendId: props.info.id}}/>);
    else
        return (<UserCard info={{...props.info, friendId: props.info.id}}> </UserCard>);
}