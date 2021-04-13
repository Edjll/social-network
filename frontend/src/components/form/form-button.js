import './form-button.css';

export const FormButton = (props) => {

    return (
        <button className={`form__button ${props.className ? props.className : ''}`} onClick={props.handleClick}>{props.children}</button>
    );
}