package com.eric.definitions.io;

import java.io.IOException;
import java.io.InputStream;

@FunctionalInterface
public interface InputStreamCallback<T> {
    T doWithInputStream(InputStream inputStream) throws IOException;

}
