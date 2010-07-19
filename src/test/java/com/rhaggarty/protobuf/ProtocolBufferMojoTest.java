/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhaggarty.protobuf;

import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Field;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author rhaggarty
 */
public class ProtocolBufferMojoTest {

    private ProtocolBufferMojo mojo;

    @Mock private File outputDirectory;

    @Mock private File sourceDirectory;
    
    @Before
    public void setup() throws IllegalAccessException, NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        when(outputDirectory.isDirectory()).thenReturn(true);
        when(sourceDirectory.isDirectory()).thenReturn(true);

        mojo = new ProtocolBufferMojo();

        // set defaults
        setField(mojo, "executable", "test-executable");
        setField(mojo, "outputDirectory", outputDirectory);
        setField(mojo, "sourceDirectory", sourceDirectory);

        mojo.setOutputTypes(new String[] {"JAVA", "CPP"});
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecuteEmptyExecutableThrowsIllegalArgException() throws Exception {
        setField(mojo, "executable", StringUtils.EMPTY);
        mojo.execute();
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void testExecuteNullOutputDirectoryThrowsIllegalArgException() throws Exception {
        setField(mojo, "outputDirectory", null);
        mojo.execute();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecuteOutputDirectoryNotDirectoryThrowsIllegalArgException() throws Exception {
        when(outputDirectory.isDirectory()).thenReturn(false);
        mojo.execute();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecuteNullSourceDirectoryThrowsIllegalArgException() throws Exception {
        setField(mojo, "sourceDirectory", null);
        mojo.execute();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecuteSourceDirectoryNotDirectoryThrowsIllegalArgException() throws Exception {
        when(sourceDirectory.isDirectory()).thenReturn(false);
        mojo.execute();
    }

    @Test (expected = IllegalArgumentException.class)
    public void testExecuteEmptyOutputTypesThrowsIllegalArgException() throws Exception {
        mojo.setOutputTypes(ArrayUtils.EMPTY_STRING_ARRAY);
        mojo.execute();
    }

    // Helper method to set fields via reflection...
    private static void setField(final Object target, final String fieldName, final Object value)
            throws IllegalAccessException, NoSuchFieldException {

        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
