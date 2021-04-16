import {Link} from "react-router-dom";
import './user-card-mini.css';

export const UserCardMini = (props) => {
    return (
        <div className={"user_card_mini"}>
            <Link to={`/user/${props.info.username}`} className={"user_card_mini__username"}>{props.info.username}</Link>
            <div className={"user_card_mini__name"} title={props.info.firstName}>{props.info.firstName}</div>
        </div>
    );
}