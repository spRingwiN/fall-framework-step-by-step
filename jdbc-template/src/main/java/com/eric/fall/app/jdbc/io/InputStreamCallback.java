package com.eric.fall.app.jdbc.io;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface InputStreamCallback<T> {
    T doWithInputStream(InputStream inputStream) throws IOException;

}
