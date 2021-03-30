import './Table.css';

export const Table = (props) => {

    return (
        <div className={`table ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}