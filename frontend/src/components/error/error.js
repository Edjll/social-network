import './error.css';

export const Error = (props) => {
    return (
        <div className={'error'}>
            {props.children}
        </div>
    );
}