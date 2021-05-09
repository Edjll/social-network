import './user-card.css';
import {Link} from "react-router-dom";
import * as React from "react";
import RequestService from "../../../services/RequestService";
import AuthService from "../../../services/AuthService";
import {FormButton} from "../../form/form-button";
import {UserFriendButton} from "../user-friend-button";
import UserFriendStatus from "../../../services/UserFriendStatus";

export class UserCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            status: props.info.status,
            friendId: props.info.friendId
        }
    }

    handleAddToFriends(response) {
        this.setState({status: UserFriendStatus.SUBSCRIBER});
        if (this.props.handleAddToFriends) this.props.handleAddToFriends(response);
    }

    handleRemoveFromFriends(response) {
        this.setState({status: null, friendId: null});
        if (this.props.handleRemoveFromFriends) this.props.handleRemoveFromFriends(response);
    }

    handleAcceptRequest(response) {
        this.setState({status: UserFriendStatus.FRIEND});
        if (this.props.handleAcceptRequest) this.props.handleAcceptRequest(response);
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
                            : <UserFriendButton handleAddToFriends={(response) => this.handleAddToFriends(response)}
                                                handleRemoveFromFriends ={(response) => this.handleRemoveFromFriends(response)}
                                                handleAcceptRequest={(response) => this.handleAcceptRequest(response)}
                                                id={this.props.info.id}
                                                friendId={this.state.friendId}
                                                status={this.state.status}/>
                    }
                </div>
            </div>
        );
    }
}