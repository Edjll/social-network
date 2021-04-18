import React from "react";
import "./form-select.css";
import {HiddenInfo} from "../hidden-info/hidden-info";

export class FormSelect extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            search: this.props.value && this.props.value.text ? this.props.value.text : "",
            options: this.props.options ? this.props.options : [],
            selected: !!this.props.value,
            active: false
        }
        if (this.state.search !== '') {
            this.state.options = this.state.options.filter(option => option.text.toLowerCase().includes(this.state.search.toLowerCase()));
        }
    }

    handleClick(option) {
        this.handleChangeText(option.text, true);
        this.setState({selected: true});
        if (this.props.handleChange) this.props.handleChange(option);
    }

    handleChangeText(search, clicked = false) {
        if (!clicked && this.state.selected === true) {
            this.setState({selected: false});
            if (this.props.handleChange) this.props.handleChange(null);
        }
        if (search !== '' && this.props.options) {
            this.setState({
                options: this.props.options.filter(option => option.text.toLowerCase().includes(search.toLowerCase()))
            });
        } else if (this.state.search !== '') {
            this.setState({options: this.props.options});
        }
        this.setState({search: search});
    }

    handleClear() {
        this.handleChangeText('');
    }

    handleFocus() {
        this.setState({active: true});
    }

    handleBlur() {
        this.setState({active: false});
    }

    render() {

        return (
            <div className={"form__select"}>
                <span className={"form__select__title"}>{this.props.title}</span>
                <div className={"form__select__search"}>
                    <input className={"form__select__search__input"}
                           value={this.state.search}
                           onChange={(e) => this.handleChangeText(e.target.value)}
                           onFocus={this.handleFocus.bind(this)}
                           onBlur={this.handleBlur.bind(this)}
                    />
                    {
                        this.state.search !== ""
                            ?   <div className={"form__select__search__clear"} onClick={this.handleClear.bind(this)}>×</div>
                            : ''
                    }
                    {
                        this.props.error
                            ? <HiddenInfo className={'form__select__error'} text={'❌'} hidden={this.props.error}/>
                            : ''
                    }
                </div>
                {
                    this.state.active && this.state.options.length > 0
                        ? <div className="form__select__options">
                            {
                                this.state.options.map(option => <span key={option.key}
                                                                       className="form__select__options__value"
                                                                       onMouseDown={() => this.handleClick(option)}>{option.text}</span>)
                            }
                        </div>
                        : ""
                }
            </div>
        )
    }
}