package com.rhaggarty.protobuf;

import org.junit.Assert;
import org.junit.Test;

public class ProtoOutputTypeTest {

    @Test
    public void testValuesOf() {
        final String[] validTypes = new String[] {"CPP", "PYTHON"};
        final ProtoOutputType[] values = ProtoOutputType.valuesOf(validTypes);
        
        Assert.assertTrue(values.length == validTypes.length);
        Assert.assertEquals(ProtoOutputType.CPP, values[0]);
        Assert.assertEquals(ProtoOutputType.PYTHON, values[1]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testValuesOfEmptyArrayThrowsIllegalArgException() {
        ProtoOutputType.valuesOf(new String[0]);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testValuesOfNullArrayThrowsIllegalArgException() {
        ProtoOutputType.valuesOf(null);
    }
}
