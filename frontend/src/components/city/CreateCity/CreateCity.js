import * as React from "react";
import '../CityForm.css';
import {Input} from "../../form/input/Input";
import {Select} from "../../form/select/Select";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class CreateCity extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            loadQueue: 1,
            title: '',
            countryId: null,
            errors: {
                title: null,
                country: null
            }
        }
    }

    componentDidMount() {
        document.body.style.overflow = 'hidden';
        RequestService.getAxios().get(RequestService.URL + "/country/all")
            .then(response => {
                this.setState({
                    countries: response.data.map(option => {
                        return {key: option.id, text: option.title}
                    }).sort((a, b) => a.text > b.text),
                    loadQueue: this.state.loadQueue - 1
                });
            });
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + "/city/save", {
            title: this.state.title,
            countryId: this.state.countryId
        })
            .then(() => this.handleClose())
            .catch(error => this.setState({
                errors: {
                    title: error.response.data.errors.title,
                    country: error.response.data.errors.countryId
                }
            }));
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {...this.state.errors, title: null}});
    }

    handleSelectCountry(value) {
        if (value === null) this.setState({countryId: null});
        else this.setState({countryId: value.key, errors: {...this.state.errors, country: null}});
    }

    render() {

        return (
            <div className={"city-form"}>
                {
                    this.state.loadQueue === 0
                        ?   <form className={"city-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                                <h1 className={"city-form__title"}>Create city</h1>
                                <Input handleChange={this.handleChangeTitle.bind(this)} label={"title"} error={this.state.errors.title}/>
                                <Select options={this.state.countries} label={"country"} onChange={this.handleSelectCountry.bind(this)} error={this.state.errors.country}/>
                                <Button text={"Save"} className={"city-form__button"}/>
                            </form>
                        :   ''
                }
                <div className={"city-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}