const create = (className, context, load) => {
    if (context.state.lastSize > 0) {
        context.observer = new IntersectionObserver(
            entries => entries.forEach(entry => {
                if (entry.isIntersecting) {
                    context.observer.unobserve(entry.target);
                    if (context.state.lastSize === context.state.size) {
                        context.setState({page: context.state.page + 1}, () =>
                            load.bind(context)(() => {
                                if (document.querySelector(`${className}`)) {
                                    context.observer.observe(document.querySelector(`${className}`));
                                }
                            })
                        );
                    }
                }
            }),
            {
                threshold: 0.9
            }
        );
        if (document.querySelector(`${className}`)) {
            context.observer.observe(document.querySelector(`${className}`));
        }
    }
}

const IntersectionObserverService = {
    create
}

export default IntersectionObserverService;