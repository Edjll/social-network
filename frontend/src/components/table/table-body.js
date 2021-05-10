import './table-body.css';

export const TableBody = (props) => {

    return (
        <div className={`table__body ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}