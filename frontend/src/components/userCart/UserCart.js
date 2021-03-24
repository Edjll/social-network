import './UserCart.css';
import '../form/button/Button.css';
import {Link} from "react-router-dom";

export const UserCart = (props) => {
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
                <Link to={`/user/${props.info.username}/message`} className={"button user-cart__action__item"}>Send
                    message</Link>
            </div>
        </div>
    );
}