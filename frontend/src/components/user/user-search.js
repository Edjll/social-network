import * as React from "react";
import RequestService from "../../services/RequestService";
import {SearchPage} from "../search/search-page";
import {UserCard} from "./user-card/user-card";

export class UserSearch extends React.Component {

    loadUsers(callback) {
        RequestService
            .getAxios()
            .get(RequestService.URL + `/users`, {
                params: {
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    countryId: this.state.countryId,
                    cityId: this.state.cityId,
                    page: this.state.page,
                    size: this.state.size
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
        return <UserCard key={key} info={info}/>
    }

    render() {
        return (
            <SearchPage loadUsers={this.loadUsers} card={this.component.bind(this)}>
                <h3>Users</h3>
            </SearchPage>
        );
    }
}