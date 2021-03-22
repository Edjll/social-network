import './UserCart.css';
import '../form/button/Button.css';
import AuthService from "../../services/AuthService";
import {Link} from "react-router-dom";

export const UserCart = (props) => {
    return (
          <div className={"user-cart"}>
              <div className={"user-cart__info"}>
                  <Link to={`/user/${props.info.username}`} className={"user-cart__info__fio"}>{props.info.firstName} {props.info.lastName}</Link>
                  {
                      props.info.city
                          ? <p className={"user-cart__info__city"}>{props.info.city}</p>
                          : ""
                  }
              </div>
              <div className={"user-cart__action"}>
                  {
                      AuthService.isAuthenticated() && AuthService.getId() !== props.info.id
                          ? <Link to={"#"} className={"button user-cart__action__item"}>Send message</Link>
                          : ""
                  }
              </div>
          </div>
    );
}