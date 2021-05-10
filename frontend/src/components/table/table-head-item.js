import './table-head-item.css';

export const TableHeadItem = (props) => {

    return (
        <div className={`table__head__item ${props.className ? props.className : ''}`}>
            {props.children}
            {
                props.handleClick
                    ?   <div onClick={() => props.handleClick(props.name,props.onlySort)}>
                            <span
                                className={`table__head__item__up-arrow ${props.order === 'asc' ? 'table__head__item__up-arrow-active' : ''}`}
                                >🠕</span>
                            <span
                                className={`table__head__item__down-arrow ${props.order === 'desc' ? 'table__head__item__down-arrow-active' : ''}`}
                                >🠗</span>
                        </div>
                    : ''
            }
        </div>
    );
}