import './Button.css';

export const Button = (props) => {
    return (
        <button
            className={`button ${props.className ? props.className : ''}`}
            onClick={() => { if (props.handleClick) props.handleClick() }}
        >{props.text}</button>
    );
}