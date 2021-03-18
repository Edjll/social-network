import AuthService from "../../services/AuthService";
import RequestService from "../../services/RequestService";
import React from "react";

export class Home extends React.Component {

    constructor(props) {
        super(props);

        this.state = {message: ""}
    }

    componentDidMount() {
        let path;

        if (!AuthService.authenticated()) {
            path = "anonymous";
        } else if (AuthService.hasRole([AuthService.Role.USER])) {
            path = "user";
        } else {
            path = "admin";
        }

        RequestService.getAxios().get(RequestService.URL + path)
            .then(response => this.setState({message: response.data}))
    }

    render() {
        return (
            <p>{ this.state.message }</p>
        )
    }
}