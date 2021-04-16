import * as React from "react";
import RequestService from "../../services/RequestService";
import {SearchPage} from "../search/search-page";
import {UserSubscriberCard} from "./user-card/user-subscriber-card";

export class UserSubscribers extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: new URLSearchParams(this.props.location.search).get("id")
        }
    }

    loadUsers(callback) {
        RequestService.getAxios().get(RequestService.URL + `/users/${this.state.id}/subscribers`, {
            params: {
                firstName: this.state.firstName,
                lastName: this.state.lastName,
                countryId: this.state.countryId,
                cityId: this.state.cityId,
                page: this.state.page,
                size: this.state.pageSize
            }
        })
            .then(response => this.setState({
                users: [...this.state.users, ...response.data.content],
                totalPages: response.data.totalPages
            }, () => {
                if (callback) callback()
            }))
    }

    component(key, info) {
        return <UserSubscriberCard key={key} info={info} userId={new URLSearchParams(this.props.location.search).get("id")}/>
    }

    render() {
        return (
            <SearchPage id={this.state.userId}
                        loadUsers={this.loadUsers}
                        card={this.component.bind(this)}>
                <h3>Subscribers</h3>
            </SearchPage>
        );
    }
}