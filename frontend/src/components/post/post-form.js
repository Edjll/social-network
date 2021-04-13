import * as React from "react";
import './post-form.css';

export class PostForm extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            text: this.props.text ? this.props.text: ""
        };
        this.inputRef = React.createRef();
        this.buttonRef = React.createRef();
    }

    componentDidMount() {
        this.inputRef.current.innerText = this.state.text;
    }

    handleChangeText(e) {
        this.setState({text: e.target.innerText});
    }

    handleClickClear() {
        this.setState({text: ""});
        this.inputRef.current.innerText = "";
    }

    handleSubmit(e) {
        e.preventDefault();
        this.props.handleSubmit(this.state.text);
        this.handleClickClear();
    }

    handleKeyDown(e) {
        if (e.code === 'Enter' && !e.shiftKey) {
            e.preventDefault();
            this.buttonRef.current.click();
        }
    }

    render() {
        return (
            <form className={"post__form"} onSubmit={this.handleSubmit.bind(this)}>
                <div className={"post__form__input"}
                     contentEditable={"true"}
                     onInput={this.handleChangeText.bind(this)}
                     ref={this.inputRef}
                     onKeyDown={this.handleKeyDown.bind(this)}/>
                <div className={"post__form__actions"}>
                    <div className={"post__form__actions__clear"} onClick={this.handleClickClear.bind(this)}>Clear</div>
                    <button className={"post__form__actions__save"} ref={this.buttonRef}>Save</button>
                </div>
            </form>
        );
    }
}