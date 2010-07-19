package com.rhaggarty.protobuf.type;

import org.apache.commons.lang.Validate;

/**
 * A representation of the available compiler output types.
 * 
 * @author rhaggarty
 */
public enum ProtoOutputType {

    JAVA("java_out"),
    CPP("cpp_out");

    private final String arg;

    private ProtoOutputType(final String arg) {
        this.arg = arg;
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
