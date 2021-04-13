import './card-header.css';

export const CardHeader = (props) => {
    return (
        <div className={`card__header ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}