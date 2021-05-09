import './messenger-message.css';
import {Link} from "react-router-dom";
import AuthService from "../../services/AuthService";
import {HiddenInfo} from "../hidden-info/hidden-info";

export const MessengerMessage = (props) => {
    return (
        <div className={`messenger_message ${AuthService.getId() === props.data.sender.id ? "messenger_message-right" : ""}`}>
            {
                props.data.modifiedDate
                    ? <HiddenInfo text={"✎"} hidden={new Date(props.data.modifiedDate).toLocaleString()}/>
                    : ""
            }
            <div className={"messenger_message__wrapper"}>
                <div className={"messenger_message__info"}>
                    <Link to={`/user/${props.data.sender.username}`}
                          className={"messenger_message__info__user"}>{props.data.sender.firstName} {props.data.sender.lastName}</Link>
                    <span className={"messenger_message__info__date"}>{new Date(props.data.createdDate).toLocaleString()}</span>
                </div>
                <pre className={"messenger_message__text"}>{props.data.text}</pre>
                {
                    AuthService.getId() === props.data.sender.id
                        ? <div className={"messenger_message__actions"}>
                            <div className={"messenger_message__edit"} onClick={() => props.handleEdit(props.data)}>✎</div>
                            <div className={"messenger_message__delete"} onClick={() => props.handleDelete(props.data)}>🗑</div>
                        </div>
                        : ""
                }
            </div>
        </div>
    )
}