import '../card/card.css';
import './form.css';
import {LoadingAnimation} from "../loading-animation/loading-animation";

export const Form = (props) => {

    const handleSubmit = (e) => {
        e.preventDefault();
        if (props.handleSubmit) props.handleSubmit(e);
    }

    return (
        <form className={`card form ${props.className ? props.className : ''}`} onSubmit={handleSubmit}>
            {props.children}
            {
                props.loading
                    ?   <div className={'form__loading'}><LoadingAnimation/></div>
                    :   ''
            }
        </form>
    );
}