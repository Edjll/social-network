import * as React from "react";
import {Input} from "../form/input/Input";
import {Select} from "../form/select/Select";
import {Button} from "../form/button/Button";
import './Register.css';
import RequestService from "../../services/RequestService";
import AuthService from "../../services/AuthService";

export class Register extends React.Component {

    constructor(props) {
        super(props);

        this.state = {
            username: null,
            email: null,
            firstName: null,
            lastName: null,
            birthday: null,
            password: null,
            country: null,
            countries: [],
            city: null,
            cities: [],
            errors: {
                username: null,
                email: null,
                firstName: null,
                lastName: null,
                password: null
            },
            loadQueue: 1
        }
    }

    componentDidMount() {
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    })
                        .sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    handleChangeUsername(value) {
        this.setState({username: value, errors: {...this.state.errors, username: null}});
    }

    handleChangeEmail(value) {
        this.setState({email: value, errors: {...this.state.errors, email: null}});
    }

    handleChangeFirstName(value) {
        this.setState({firstName: value, errors: {...this.state.errors, firstName: null}});
    }

    handleChangeLastName(value) {
        this.setState({lastName: value, errors: {...this.state.errors, lastName: null}});
    }

    handleChangeBirthday(value) {
        this.setState({birthday: value, errors: {...this.state.errors, birthday: null}});
    }

    handleChangePassword(value) {
        this.setState({password: value, errors: {...this.state.errors, password: null}});
    }

    handleChangeCountry(value) {
        if (value === null) this.setState({country: null, city: null})
        else {
            RequestService.getAxios().get(RequestService.URL + `/city/all`, {params: {countryId: value.key}})
                .then(response => this.setState({
                        country: value.key,
                        cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
        }
    }

    handleChangeCity(value) {
        if (value === null) this.setState({city: null});
        else this.setState({city: value.key});
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + '/user/register', {
            username: this.state.username,
            email: this.state.email,
            firstName: this.state.firstName,
            lastName: this.state.lastName,
            birthday: this.state.birthday,
            credentials: [{value: this.state.password ? this.state.password : ''}],
            cityId: this.state.city
        })
            .then(() => AuthService.login())
            .catch(error => this.setState({
                errors: {
                    ...this.state.errors,
                    ...error.response.data.errors,
                    password: error.response.data.errors['credentials[0].value']
                }
            }))
    }

    render() {
        return (
            <div className={"register"}>
                <h1 className={"register__title"}>Registration</h1>
                <form className={"register__form"} onSubmit={this.handleSubmit.bind(this)}>
                    <Input label={"username*"} error={this.state.errors.username}
                           handleChange={this.handleChangeUsername.bind(this)}/>
                    <Input label={"email*"} error={this.state.errors.email}
                           handleChange={this.handleChangeEmail.bind(this)} type={"email"}/>
                    <Input label={"first name*"} error={this.state.errors.firstName}
                           handleChange={this.handleChangeFirstName.bind(this)}/>
                    <Input label={"last name*"} error={this.state.errors.lastName}
                           handleChange={this.handleChangeLastName.bind(this)}/>
                    {
                        this.state.loadQueue === 0
                            ? <Select label={"country"} options={this.state.countries}
                                      onChange={this.handleChangeCountry.bind(this)}/>
                            : ''
                    }
                    {
                        this.state.country
                            ? <Select label={"city"} onChange={this.handleChangeCity.bind(this)}
                                      options={this.state.cities}/>
                            : ''
                    }
                    <Input label={"birthday"} handleChange={this.handleChangeBirthday.bind(this)} type={"date"}/>
                    <Input label={"password*"} error={this.state.errors.password}
                           handleChange={this.handleChangePassword.bind(this)} type={"password"}/>
                    <Button text={"Register"}/>
                </form>
            </div>
        );
    }
}