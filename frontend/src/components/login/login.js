import * as React from "react";
import {Form} from "../form/form";
import {CardHeader} from "../card/card-header";
import {CardBody} from "../card/card-body";
import {FormInput} from "../form/form-input";
import {CardFooter} from "../card/card-footer";
import {FormButton} from "../form/form-button";
import AuthService from "../../services/AuthService";
import './login.css';
import {Redirect} from "react-router-dom";

export class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        }
    }

    handleChangeUsername(username) {
        this.setState({username: username});
    }

    handleChangePassword(password) {
        this.setState({password: password});
    }

    handleLogin() {
        AuthService.login(this.state.username, this.state.password);
    }

    render() {
        if (AuthService.isAuthenticated()) {
            return (<Redirect to={{pathname: '/',}}/>);
        }
        return (
            <Form handleSubmit={this.handleLogin.bind(this)} className={"login_form"}>
                <CardHeader>
                    <h1>Login</h1>
                </CardHeader>
                <CardBody>
                    <FormInput value={this.state.username} title={"username"} handleChange={this.handleChangeUsername.bind(this)}/>
                    <FormInput value={this.state.password} title={"password"} type={"password"} handleChange={this.handleChangePassword.bind(this)}/>
                </CardBody>
                <CardFooter>
                    <FormButton>Login</FormButton>
                </CardFooter>
            </Form>
        );
    }
}