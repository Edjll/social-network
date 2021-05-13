import * as React from "react";
import {UserCard} from "./user-card";

export const UserFriendCard = (props) => {

    return (
        <UserCard info={props.info}/>
    );
}