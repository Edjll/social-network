import * as React from "react";
import {UserCard} from "./user-card";
import {FormButton} from "../../form/form-button";
import AuthService from "../../../services/AuthService";

export const UserSubscriberCard = (props) => {

    const button = () => {
        let friendButton = '';

        if (AuthService.isAuthenticated() && AuthService.getId() === this.props.userId) {
            switch (this.state.status) {
                case 0:
                    friendButton = <FormButton handleClick={() => this.handleRemoveFromFriends(this.props.info.id)}>Remove from friends</FormButton>
                    break;
                case 1:
                    friendButton =
                        <FormButton handleClick={() => this.handleRemoveFromFriends(this.props.info.id)}>Cancel request</FormButton>
                    break;
                default:
                    friendButton =
                        <FormButton handleClick={() => this.handleAddToFriends(this.props.info.id)}>Add to friends</FormButton>
            }
        }

        return friendButton;
    }

    return (
        <UserCard button={button} info={props.info}/>
    );
}