import './table-row.css';

export const TableRow = (props) => {

    return (
        <div className={`table__body__row ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}