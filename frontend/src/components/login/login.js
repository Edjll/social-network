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
import Validator from "../../services/Validator";
import validation from "../../services/validation.json";

export class Login extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            errors: null
        }
    }

    handleChangeUsername(username) {
        this.setState({username: username});
    }

    handleChangePassword(password) {
        this.setState({password: password});
    }

    handleLogin() {
        if (this.validate() === 0) {
            AuthService.login(this.state.username, this.state.password);
        }
    }

    validate() {
        let size = 0;
        let errors = {...this.state.errors};
        const usernameError = Validator.validate('Username', this.state.username, validation.user.username.params);
        if (usernameError) {
            errors = {...errors, username: usernameError};
            size++;
        }

        const passwordError = Validator.validate('Password', this.state.password, validation.user.password.params);
        if (passwordError) {
            errors = {...errors, password: passwordError};
            size++;
        }

        this.setState({errors: errors});
        return size;
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
                    <FormInput error={this.state.errors ? this.state.errors.username : null} value={this.state.username} title={"username"} handleChange={this.handleChangeUsername.bind(this)}/>
                    <FormInput error={this.state.errors ? this.state.errors.password : null} value={this.state.password} title={"password"} type={"password"} handleChange={this.handleChangePassword.bind(this)}/>
                </CardBody>
                <CardFooter>
                    <FormButton>Login</FormButton>
                </CardFooter>
            </Form>
        );
    }
}