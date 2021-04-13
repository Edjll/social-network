import './table-row-item.css';

export const TableRowItem = (props) => {

    return (
        <div className={`table__body__row__item ${props.className ? props.className : ''}`}>
            {props.children}
        </div>
    );
}