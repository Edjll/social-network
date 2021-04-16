import './user-card.css';
import {Link} from "react-router-dom";
import * as React from "react";
import RequestService from "../../../services/RequestService";
import AuthService from "../../../services/AuthService";
import {FormButton} from "../../form/form-button";

export class UserCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            status: props.info.status,
            friendId: props.info.friendId
        }
    }

    handleAddToFriends() {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/users/${this.props.info.id}/friends`)
            .then(response => {
                this.setState({status: 1});
                if (this.props.handleAddToFriends) this.props.handleAddToFriends(response);
            })
    }

    handleRemoveFromFriends() {
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/users/${this.props.info.id}/friends`)
            .then(response => {
                this.setState({status: null, friendId: null});
                if (this.props.handleRemoveFromFriends) this.props.handleRemoveFromFriends(response);
            })
    }

    handleAcceptRequest() {
        RequestService
            .getAxios()
            .put(RequestService.URL + `/users/${this.props.info.id}/friends`)
            .then(response => {
                this.setState({status: 0});
                if (this.props.handleAcceptRequest) this.props.handleAcceptRequest(response);
            })
    }

    button() {
        let friendButton = '';

        if (AuthService.isAuthenticated() && AuthService.getId() !== this.props.info.id) {
            switch (this.state.status) {
                case 0:
                    friendButton = <FormButton handleClick={() => this.handleRemoveFromFriends()}>Remove from friends</FormButton>
                    break;
                case 1:
                    if (this.state.friendId === this.props.info.id) {
                        friendButton =
                            <FormButton handleClick={() => this.handleAcceptRequest()}>Accept request</FormButton>
                    } else {
                        friendButton =
                            <FormButton handleClick={() => this.handleRemoveFromFriends()}>Cancel request</FormButton>
                    }
                    break;
                default:
                    friendButton =
                        <FormButton handleClick={() => this.handleAddToFriends()}>Add to friends</FormButton>
            }
        }

        return friendButton;
    }

    render() {

        return (
            <div className={"user_card"}>
                <div className={"user_card__info"}>
                    <Link to={`/user/${this.props.info.username}`}
                          className={"user_card__info__name"}>{this.props.info.firstName} {this.props.info.lastName}</Link>
                    {
                        this.props.info.city
                            ? <p className={"user_card__info__city"}>{this.props.info.city}</p>
                            : ""
                    }
                </div>
                <div className={"user_card__action"}>
                    {
                        this.props.children
                            ? this.props.children
                            : this.button()
                    }
                </div>
            </div>
        );
    }
}