import './Button.css';

export const Button = (props) => {
    return (
        <button className={`button ${props.className}`}>{props.text}</button>
    );
}