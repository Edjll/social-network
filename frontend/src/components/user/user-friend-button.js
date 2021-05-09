import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";
import {FormButton} from "../form/form-button";
import UserFriendStatus from "../../services/UserFriendStatus";

export const UserFriendButton = (props) => {
    const handleAddToFriends = () => {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/users/${props.id}/friends`)
            .then(response => {
                if (props.handleAddToFriends) props.handleAddToFriends(response);
            })
    }

    const handleRemoveFromFriends = () => {
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/users/${props.id}/friends`)
            .then(response => {
                if (props.handleRemoveFromFriends) props.handleRemoveFromFriends(response);
            })
    }

    const handleAcceptRequest = () => {
        RequestService
            .getAxios()
            .put(RequestService.URL + `/users/${props.id}/friends`)
            .then(response => {
                if (props.handleAcceptRequest) props.handleAcceptRequest(response);
            })
    }

    if (AuthService.isAuthenticated() && AuthService.getId() !== props.id) {
        switch (props.status) {
            case UserFriendStatus.FRIEND:
                return <FormButton handleClick={() => handleRemoveFromFriends()}>Remove from friends</FormButton>;
            case UserFriendStatus.SUBSCRIBER:
                if (props.friendId === props.id) {
                    return <FormButton handleClick={() => handleAcceptRequest()}>Accept request</FormButton>;
                } else {
                    return <FormButton handleClick={() => handleRemoveFromFriends()}>Cancel request</FormButton>;
                }
            default:
                return <FormButton handleClick={() => handleAddToFriends()}>Add to friends</FormButton>;
        }
    }

    return '';
}