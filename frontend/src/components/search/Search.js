import React from "react";
import {Input} from "../form/input/Input";
import {Select} from "../form/select/Select";
import {Button} from "../form/button/Button";
import './Search.css';
import RequestService from "../../services/RequestService";
import {UserCart} from "../userCart/UserCart";
import {Spinner} from "../spinner/Spinner";

export class Search extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            firstName: "",
            lastName: "",
            city: {
                id: null,
                title: null
            },
            country: {
                id: null,
                title: null
            },
            selectedCountry: false,
            users: [],
            loadQueue: 1
        }
    }

    handleSubmit(e) {
        e.preventDefault();
        this.getUsers();
    }

    getUsers() {
        RequestService.getAxios()
            .get(RequestService.URL + `/user/search`, {
                params: {
                    firstName: this.state.firstName,
                    lastName: this.state.lastName,
                    countryId: this.state.country.id,
                    cityId: this.state.city.id
                }
            }).then(response => this.setState({users: response.data}));
    }

    handleChangeFirstName(value) {
        this.setState({firstName: value});
    }

    handleChangeLastName(value) {
        this.setState({lastName: value});
    }

    handleChangeCountry(value) {
        if (value != null) {
            RequestService.getAxios().get(RequestService.URL + `/city/all?countryId=${value.key}`)
                .then(response =>
                    this.setState({
                        selectedCountry: true, cities: response.data.map(option => {
                            return {key: option.id, text: option.title}
                        })
                    })
                );
            this.setState({
                country: {
                    id: value.key,
                    title: value.text
                }
            })
        } else if (this.state.selectedCountry === true) {
            this.setState({
                selectedCountry: false,
                city: {
                    id: null,
                    title: null,
                },
                country: {
                    id: null,
                    title: null
                }
            });
        }
    }

    handleChangeCity(value) {
        if (value !== null) {
            this.setState({
                city: {
                    id: value.key,
                    title: value.text,
                }
            });
        } else {
            this.setState({
                city: {
                    id: null,
                    title: null,
                }
            });
        }
    }

    componentDidMount() {
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
        this.getUsers();
    }

    render() {

        return (
            <div className={"search"}>
                <div className={"search__result"}>
                    {
                        this.state.users.map(user => <UserCart key={user.id} info={user}/>)
                    }
                </div>
                <div className={"search__form"}>
                    {
                        this.state.loadQueue === 0
                            ? <form onSubmit={this.handleSubmit.bind(this)}>
                                <Input label={"First name"} handleChange={this.handleChangeFirstName.bind(this)}/>
                                <Input label={"Last name"}/>
                                <Select name="country" label="Country" options={this.state.countries}
                                        onChange={this.handleChangeCountry.bind(this)}/>
                                {this.state.selectedCountry ? <Select name="city" label="City" options={this.state.cities}
                                                                      onChange={this.handleChangeCity.bind(this)}/> : ""}
                                <Button text={"Search"}/>
                            </form>
                            : <Spinner/>
                    }
                </div>
            </div>
        );
    }
}