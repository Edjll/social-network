import * as React from "react";
import {UserCard} from "./user-card";

export const UserSubscriberCard = (props) => {

    return (
        <UserCard info={{...props.info, friendId: props.info.id}}/>
    );
}