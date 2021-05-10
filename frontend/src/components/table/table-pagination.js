import './table-pagination.css';

export const TablePagination = (props) => {

    const buttons = [];
    let maxPage = 1;

    if (props.loading) {
        buttons.push(<div key={'table__pagination__loading'} className={'table__pagination__loading'}>
            <div className={'table__pagination__loading__item'}/>
            <div className={'table__pagination__loading__item'}/>
            <div className={'table__pagination__loading__item'}/>
            <div className={'table__pagination__loading__item'}/>
            <div className={'table__pagination__loading__item'}/>
        </div>);
    } else {
        let leftIndex, rightIndex;
        maxPage = props.maxPage > 0 ? props.maxPage : 1;

        if (props.maxButtons < props.maxPage) {
            let buttonCount = props.maxButtons < maxPage ? props.maxButtons : maxPage;
            let buttonCountAround = Math.floor((buttonCount - ((buttonCount + 1) % 2))/ 2);

            let maxRightCount = maxPage - props.page - 1;
            let maxLeftCount = props.page;

            if (maxLeftCount < buttonCountAround) leftIndex = 0;
            else leftIndex = props.page - buttonCountAround;

            if (maxRightCount < buttonCountAround) rightIndex = props.page + maxRightCount;
            else rightIndex = props.page + buttonCountAround;

            let leftIndexTmp = leftIndex;
            leftIndex -= buttonCountAround - (rightIndex - props.page);
            rightIndex += buttonCountAround - (props.page - leftIndexTmp);
        } else {
            leftIndex = 0;
            rightIndex = props.maxPage - 1;
        }

        for (let i = leftIndex; i <= rightIndex; i++) {
            if (props.page === i) {
                buttons.push(
                    <div
                        key={i}
                        className={`table__pagination__page table__pagination__item-active`}
                    >{i + 1}</div>
                );
            } else {
                buttons.push(
                    <div
                        key={i}
                        className={`table__pagination__page`}
                        onClick={() => props.handleClick(i)}
                    >{i + 1}</div>
                );
            }
        }
    }

    return (
        <div className={`table__pagination ${props.className ? props.className : ''}`}>
            {
                props.page === 0
                    ?   <div className={`table__pagination__previous table__pagination__item-disable`}>{'<'}</div>
                    :   <div
                            className={`table__pagination__previous`}
                            onClick={() => props.handleClick(props.page - 1)}
                        >{'<'}</div>
            }

            { buttons }

            {
                props.page === maxPage - 1
                    ?   <div className={`table__pagination__next table__pagination__item-disable`}>{'>'}</div>
                    :   <div
                        className={`table__pagination__next`}
                        onClick={() => props.handleClick(props.page + 1)}
                    >{'>'}</div>
            }
        </div>
    );
}