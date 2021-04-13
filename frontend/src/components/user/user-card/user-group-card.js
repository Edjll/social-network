import {Link} from "react-router-dom";
import './user-group-card.css';


export const UserGroupCard = (props) => {

    return (
        <div className={"user_group_card"}>
            <Link to={`/group/${props.address}`} className={"user_group_card__address"}>
                {props.address}
            </Link>
            <Link to={`/group/${props.address}`} className={"user_group_card__title"} title={props.title}>
                {props.title}
            </Link>
        </div>
    );
}