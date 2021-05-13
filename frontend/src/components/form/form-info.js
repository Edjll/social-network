import './form-info.css';

export const FormInfo = (props) => {

    return (
        <div className={"form__info"}>
            {props.children}
        </div>
    );
}