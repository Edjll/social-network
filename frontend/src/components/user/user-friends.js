import * as React from "react";
import RequestService from "../../services/RequestService";
import {SearchPage} from "../search/search-page";
import {UserFriendCard} from "./user-card/user-friend-card";

export class UserFriends extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            userId: new URLSearchParams(this.props.location.search).get("id")
        }
    }

    loadUsers(callback) {
        RequestService.getAxios().get(RequestService.URL + `/users/${this.state.id}/friends`, {
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
        return <UserFriendCard key={key} info={info}
                               userId={new URLSearchParams(this.props.location.search).get("id")}/>
    }

    render() {
        return (
            <SearchPage userId={this.state.userId} id={this.state.userId} loadUsers={this.loadUsers}
                        card={this.component.bind(this)}>
                <h3>Friends</h3>
            </SearchPage>
        );
    }
}