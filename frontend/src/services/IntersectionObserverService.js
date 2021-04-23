const create = (className, context, load) => {
    if (context.state.totalPages > 1) {
        context.observer = new IntersectionObserver(
            entries => entries.forEach(entry => {
                if (entry.isIntersecting) {
                    context.observer.unobserve(entry.target);
                    if (context.state.page + 1 < context.state.totalPages) {
                        context.setState({page: context.state.page + 1}, () =>
                            load.bind(context)(() => {
                                context.observer.observe(document.querySelector(`${className}`));
                            })
                        );
                    }
                }
            }),
            {
                threshold: 0.9
            }
        );
        context.observer.observe(document.querySelector(`${className}`));
    }
}

const IntersectionObserverService = {
    create
}

export default IntersectionObserverService;