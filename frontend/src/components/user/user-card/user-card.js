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
            status: props.info.status
        }
    }

    handleAddToFriends() {
        RequestService.getAxios().post(RequestService.URL + "/user/friend/save", {
            userId: this.props.info.id
        })
            .then(response => {
                this.setState({status: 1});
                if (this.props.handleAddToFriends) this.props.handleAddToFriends(response);
            })
    }

    handleRemoveFromFriends() {
        RequestService.getAxios().delete(RequestService.URL + "/user/friend/delete", {
            params: {
                userId: this.props.info.id
            }
        })
            .then(response => {
                this.setState({status: null});
                if (this.props.handleRemoveFromFriends) this.props.handleRemoveFromFriends(response);
            })
    }

    button() {
        let friendButton = '';

        if (AuthService.isAuthenticated() && AuthService.getId() !== this.props.info.id) {
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
                    { this.props.button ? () => this.props.button() : this.button() }
                </div>
            </div>
        );
    }
}