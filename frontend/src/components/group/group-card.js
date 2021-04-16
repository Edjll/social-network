import './group-card.css';
import {Link} from "react-router-dom";
import {FormButton} from "../form/form-button";
import RequestService from "../../services/RequestService";
import * as React from "react";
import AuthService from "../../services/AuthService";

export class GroupCard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            subscribed: props.subscribed
        }
    }

    handleSubscribe() {
        RequestService
            .getAxios()
            .post(RequestService.URL + `/groups/${this.props.id}/users`)
            .then(() => this.setState({subscribed: true}))
    }

    handleUnsubscribe() {
        RequestService
            .getAxios()
            .delete(RequestService.URL + `/groups/${this.props.id}/users`)
            .then(() => this.setState({subscribed: false}))
    }

    render() {

        return (
            <div className={"group_card"}>
                <Link to={`/group/${this.props.address}`} className={"group_card__address"}>
                    {this.props.address}
                </Link>
                <div className={"group_card__info"}>
                    <Link to={`/group/${this.props.address}`} className={"group_card__title"} title={this.props.title}>
                        {this.props.title}
                    </Link>
                    <div className={"group_card__description"} title={this.props.description}>
                        {this.props.description}
                    </div>
                </div>
                <div className={"group_cart__actions"}>
                    {
                        AuthService.isAuthenticated()
                            ?   this.state.subscribed
                                    ?   <FormButton handleClick={this.handleUnsubscribe.bind(this)}>Unsubscribe</FormButton>
                                    :   <FormButton handleClick={this.handleSubscribe.bind(this)}>Subscribe</FormButton>
                            :   ''
                    }
                </div>
            </div>
        );
    }
}