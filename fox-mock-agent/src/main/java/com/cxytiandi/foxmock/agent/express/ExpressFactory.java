package com.cxytiandi.foxmock.agent.express;

public class ExpressFactory {

    private static final ThreadLocal<Express> EXPRESS_THREAD_LOCAL = new ThreadLocal<Express>() {
        @Override
        protected Express initialValue() {
            return new OgnlExpress();
        }
    };

    public static Express getExpress(Object object) {
        return EXPRESS_THREAD_LOCAL.get().reset().bind(object);
    }

}