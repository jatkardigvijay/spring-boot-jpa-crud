package com.jbd.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class JbdExceptionTest {

    @Test
    void constructor_withMessageOnly_setsMessage() {
        JbdException ex = new JbdException("something went wrong");

        assertThat(ex.getMessage()).isEqualTo("something went wrong");
        assertThat(ex.getStatusCode()).isNull();
        assertThat(ex.getObject()).isNull();
    }

    @Test
    void constructor_withMessageAndCause_setsBoth() {
        Throwable cause = new RuntimeException("root cause");
        JbdException ex = new JbdException("wrapped error", cause);

        assertThat(ex.getMessage()).isEqualTo("wrapped error");
        assertThat(ex.getCause()).isSameAs(cause);
    }

    @Test
    void constructor_withMessageAndStatus_setsMessageAndStatus() {
        JbdException ex = new JbdException("not found", HttpStatus.NOT_FOUND);

        assertThat(ex.getMessage()).isEqualTo("not found");
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(ex.getObject()).isNull();
    }

    @Test
    void constructor_withMessageStatusAndObject_setsAllFields() {
        Object payload = java.util.List.of();
        JbdException ex = new JbdException("no data", HttpStatus.OK, payload);

        assertThat(ex.getMessage()).isEqualTo("no data");
        assertThat(ex.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ex.getObject()).isSameAs(payload);
    }

    @Test
    void jbdException_isInstanceOfException() {
        JbdException ex = new JbdException("test");

        assertThat(ex).isInstanceOf(Exception.class);
    }
}