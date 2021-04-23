import './messenger-interlocutor-card.css';
import {Link} from "react-router-dom";

export const MessengerInterlocutorCard = (props) => {
    return (
        <Link to={{pathname: "/messenger", search: `id=${props.info.id}` }} className={`messenger_interlocutor_card ${props.active ? 'messenger_interlocutor_card-active' : ''}`}>
            <div className={"messenger_interlocutor_card__name"}>{props.info.firstName} {props.info.lastName}</div>
            {
                props.info.newMessages > 0
                    ?   <div className={"messenger_interlocutor_card__notification"}>{props.info.newMessages}</div>
                    :   ''
            }
        </Link>
    );
}