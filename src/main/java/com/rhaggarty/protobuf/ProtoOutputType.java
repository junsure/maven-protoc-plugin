package com.rhaggarty.protobuf;

import org.apache.commons.lang.Validate;

/**
 * A representation of the available compiler output types.
 * 
 * @author Ryan Haggarty (ryanmh at gmail dot com)
 */
public enum ProtoOutputType {

    CPP("--cpp_out"),
    JAVA("--java_out"),
    PYTHON("--python_out");

    private final String arg;

    private ProtoOutputType(final String arg) {
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    public static ProtoOutputType[] valuesOf(final String[] values) {
        Validate.notEmpty(values);

        final ProtoOutputType[] types = new ProtoOutputType[values.length];
        for (int i = 0; i < values.length; i++) {
            types[i] = ProtoOutputType.valueOf(values[i]);
        }

        return types;
    }
}
