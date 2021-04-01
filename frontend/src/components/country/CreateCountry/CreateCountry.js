import * as React from "react";
import '../CountryForm.css';
import {Input} from "../../form/input/Input";
import RequestService from "../../../services/RequestService";
import {Button} from "../../form/button/Button";

export class CreateCountry extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            countries: [],
            title: '',
            errors: {
                title: null
            }
        }
    }

    handleClose() {
        document.body.style.overflow = 'auto';
        this.props.history.goBack();
    }

    handleSubmit(e) {
        e.preventDefault();
        RequestService.getAxios().post(RequestService.URL + "/country/save", {
            title: this.state.title
        })
            .then(() => this.handleClose())
            .catch(error => this.setState({
                errors: {
                    title: error.response.data.errors.title
                }
            }));
    }

    handleChangeTitle(value) {
        this.setState({title: value, errors: {title: null}});
    }

    render() {

        return (
            <div className={"country-form"}>
                <form className={"country-form__form"} onSubmit={this.handleSubmit.bind(this)}>
                    <h1 className={"country-form__title"}>Create country</h1>
                    <Input handleChange={this.handleChangeTitle.bind(this)} label={"title"} error={this.state.errors.title}/>
                    <Button text={"Save"} className={"country-form__button"}/>
                </form>
                <div className={"country-form__button-close"} onClick={this.handleClose.bind(this)}>×</div>
            </div>
        );
    }
}