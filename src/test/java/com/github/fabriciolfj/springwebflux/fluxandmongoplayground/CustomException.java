package com.github.fabriciolfj.springwebflux.fluxandmongoplayground;

public class CustomException extends RuntimeException {

    public CustomException(final String e, final Throwable cause) {
        super(e, cause);
    }

    public CustomException(final String e) {
        super(e);
    }
}
