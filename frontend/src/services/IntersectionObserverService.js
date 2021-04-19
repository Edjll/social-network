const create = (className, context) => {
    if (context.state.totalPages > 1) {
        context.observer = new IntersectionObserver(
            entries => entries.forEach(entry => {
                if (entry.isIntersecting) {
                    context.observer.unobserve(entry.target);
                    if (context.state.page + 1 < context.state.totalPages) {
                        context.setState({page: context.state.page + 1}, () =>
                            context.loadPosts(() => {
                                context.observer.observe(document.querySelector(`.${className}:last-child`));
                            })
                        );
                    }
                }
            }),
            {
                threshold: 0.75
            }
        );
        context.observer.observe(document.querySelector(`.${className}:last-child`));
    }
}

const IntersectionObserverService = {
    create
}

export default IntersectionObserverService;