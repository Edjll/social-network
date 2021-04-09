import './UserCart.css';
import '../form/button/Button.css';
import {Link} from "react-router-dom";
import AuthService from "../../services/AuthService";
import {Button} from "../form/button/Button";
import RequestService from "../../services/RequestService";

export const UserCart = (props) => {

    let friendButton = '';

    if (AuthService.isAuthenticated()) {
        switch (props.info.status) {
            case 0:
                friendButton = <Button text={"Remove from friends"}/>
                break;
            case 1:
                friendButton = <Button text={"Cancel request"} handleClick={() => props.handleRemoveFromFriends(props.info.id)}/>
                break;
            default:
                friendButton = <Button text={"Add to friends"} handleClick={() => props.handleAddToFriends(props.info.id)}/>
        }
    }

    return (
        <div className={"user-cart"}>
            <div className={"user-cart__info"}>
                <Link to={`/user/${props.info.username}`}
                      className={"user-cart__info__fio"}>{props.info.firstName} {props.info.lastName}</Link>
                {
                    props.info.city
                        ? <p className={"user-cart__info__city"}>{props.info.city}</p>
                        : ""
                }
            </div>
            <div className={"user-cart__action"}>
                {friendButton}
            </div>
        </div>
    );
}