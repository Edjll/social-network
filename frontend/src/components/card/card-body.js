import './card-body.css';

export const CardBody = (props) => {
    return (
        <div className={`card__body ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}