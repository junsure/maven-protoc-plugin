/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhaggarty.protobuf;

import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
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

        when(outputDirectory.isFile()).thenReturn(false);
        when(sourceDirectory.isDirectory()).thenReturn(true);

        mojo = new ProtocolBufferMojo();

        // set defaults
        setField(mojo, "executable", "test-executable");
        setField(mojo, "sourceDirectory", sourceDirectory);

        mojo.setOutputTypes(Collections.singletonMap("JAVA", "."));
    }

    @Test (expected = MojoExecutionException.class)
    public void testExecuteEmptyExecutableThrowsMojoExecException() throws Exception {
        setField(mojo, "executable", StringUtils.EMPTY);
        mojo.execute();
    }
    
    @Test (expected = MojoExecutionException.class)
    public void testExecuteNullSourceDirectoryThrowsMojoExecException() throws Exception {
        setField(mojo, "sourceDirectory", null);
        mojo.execute();
    }

    @Test (expected = MojoExecutionException.class)
    public void testExecuteSourceDirectoryNotDirectoryThrowsMojoExecException() throws Exception {
        when(sourceDirectory.isDirectory()).thenReturn(false);
        mojo.execute();
    }

    @Test (expected = MojoExecutionException.class)
    public void testExecuteEmptyOutputTypesThrowsIllegalArgException() throws Exception {
        mojo.setOutputTypes(Collections.EMPTY_MAP);
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