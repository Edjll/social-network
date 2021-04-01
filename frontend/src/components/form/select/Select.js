import React from "react";
import "./Select.css"

export class Select extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            search: this.props.value && this.props.value.text ? this.props.value.text : "",
            options: this.props.options ? this.props.options : [],
            active: false,
            selected: !!this.props.value
        }
        if (this.state.search !== "") {
            this.state.options = this.state.options.filter(option => option.text.toLowerCase().includes(this.state.search.toLowerCase()));
        }
    }

    handleClick(option) {
        this.handleInput(option.text, true);
        this.setState({selected: true});
        if (this.props.onChange) this.props.onChange(option);
    }

    handleInput(search, clicked = false) {
        if (!clicked && this.state.selected === true) {
            this.setState({selected: false});
            if (this.props.onChange) this.props.onChange(null);
        }
        if (search !== '') {
            this.setState({
                options: this.props.options.filter(option => option.text.toLowerCase().includes(search.toLowerCase()))
            });
        } else if (this.state.search !== '') {
            this.setState({options: this.props.options});
        }
        this.setState({search: search});
    }

    handleFocus() {
        this.setState({active: true});
    }

    handleBlur() {
        this.setState({active: false});
    }

    handleClear() {
        this.handleInput("");
    }

    render() {

        return (
            <div className="select">
                <span className="select__label">{this.props.label}</span>
                <div className="select__search">
                    <input className="select__search__input"
                           value={this.state.search}
                           onChange={(e) => this.handleInput(e.target.value)}
                           onFocus={this.handleFocus.bind(this)}
                           onBlur={this.handleBlur.bind(this)}
                    />
                    {this.state.search !== "" ?
                        <div className="select__search__clear" onClick={this.handleClear.bind(this)}>×</div> : ""}
                    {
                        this.props.error
                            ? <div className={"select__error"}>
                                <div className={"select__error__description"}>{this.props.error}</div>
                                <div className={"select__error__icon"}>❌</div>
                            </div>
                            : ''
                    }
                </div>
                {
                    this.state.active && this.state.options.length > 0
                        ? <div className="select__options">
                            {
                                this.state.options.map(option => <span key={option.key}
                                                                       className="select__options__value"
                                                                       onMouseDown={() => this.handleClick(option)}>{option.text}</span>)
                            }
                        </div>
                        : ""
                }
            </div>
        )
    }

}