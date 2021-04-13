import './table-page-size.css';
import * as React from "react";

export class TablePageSize extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            value: this.props.value
        }
    }

    handleDownKey(e) {
        if (e.code === 'Enter') {
            e.preventDefault();
            this.handleBlur();
        }
    }

    handleChange(e) {
        this.setState({value: e.target.value});
    }

    handleBlur() {
        this.props.handleChange(this.state.value);
    }

    render() {
        return (
            <input className={`table__page-size ${this.props.className ? this.props.className : ''}`}
                   value={this.state.value} onChange={this.handleChange.bind(this)}
                   type={'number'} onBlur={this.handleBlur.bind(this)} onKeyDown={this.handleDownKey.bind(this)}/>
        );
    }
}