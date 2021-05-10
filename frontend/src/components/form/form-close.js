import './form-close.css';

export const FormClose = (props) => {

    return (
        <div className={"form__close"} onClick={props.handleClick ? props.handleClick : null}>
            ✕
        </div>
    );
}