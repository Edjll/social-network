import './card.css';

export const Card = (props) => {

    return(
        <div className={`card ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}