package com.cxytiandi.foxmock.agent.express;

public class OgnlExpressException extends Exception {

    private final String express;

    public OgnlExpressException(String express, Throwable cause) {
        super(cause);
        this.express = express;
    }

    public String getExpress() {
        return express;
    }
}
