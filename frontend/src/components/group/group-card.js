import './group-card.css';
import {Link} from "react-router-dom";

export const GroupCard = (props) => {

    return (
        <div className={"group_card"}>
            <Link to={`/group/${props.address}`} className={"group_card__address"}>
                {props.address}
            </Link>
            <div className={"group_card__info"}>
                <Link to={`/group/${props.address}`} className={"group_card__title"} title={props.title}>
                    {props.title}
                </Link>
                <div className={"group_card__description"} title={props.description}>
                    {props.description}
                </div>
            </div>
        </div>
    );
}