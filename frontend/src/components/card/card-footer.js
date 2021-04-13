import './card-footer.css';

export const CardFooter = (props) => {

    return (
        <div className={`card__footer ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}