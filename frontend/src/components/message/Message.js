import './Message.css';
import {Link} from "react-router-dom";
import AuthService from "../../services/AuthService";
import {HiddenInfo} from "../hiddenInfo/HiddenInfo";

export const Message = (props) => {
    return (
        <div className={`message ${AuthService.getId() !== props.data.sender.id ? "message-right" : ""}`}>
            <div className={"message__wrapper"}>
                <div className={"message__info"}>
                    <Link to={`/user/${props.data.sender.username}`}
                          className={"message__info__user"}>{props.data.sender.firstName} {props.data.sender.lastName}</Link>
                    <span className={"message__info__date"}>{new Date(props.data.createdDate).toLocaleString()}</span>
                    {
                        props.data.modifiedDate
                            ? <HiddenInfo text={"(ред.)"} hidden={new Date(props.data.modifiedDate).toLocaleString()}/>
                            : ""
                    }
                </div>
                <pre className={"message__text"}>{props.data.text}</pre>
            </div>
            {
                AuthService.getId() === props.data.sender.id
                    ? <div className={"message__actions"}>
                        <div className={"message__edit"} onClick={() => props.handleEdit(props.data)}>✎</div>
                        <div className={"message__delete"} onClick={() => props.handleDelete(props.data)}>🗑</div>
                    </div>
                    : ""
            }
        </div>
    )
}