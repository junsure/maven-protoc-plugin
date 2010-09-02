/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rhaggarty.protobuf;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author rhaggarty
 */
public class ProtocolBufferMojoTest {

    private ProtocolBufferMojo mojo;

    @Mock private File outputDirectory;

    @Mock private File sourceDirectory;
    
    @Mock private ProtoCommandBuilder builder;
    
    @Mock MavenProject project;
    
    @Before
    public void setup() throws IllegalAccessException, MojoExecutionException, NoSuchFieldException {
        MockitoAnnotations.initMocks(this);

        when(outputDirectory.isFile()).thenReturn(false);
        when(sourceDirectory.isDirectory()).thenReturn(true);

        mojo = new ProtocolBufferMojo();

        setField(mojo, "builder", builder);
        setField(mojo, "executable", "test-executable");
        setField(mojo, "sourceDirectory", sourceDirectory);
        setField(mojo, "project", project);

        mojo.setOutputTypes(Collections.singletonMap("CPP", "."));
    }

    @Test @SuppressWarnings("unchecked")
    public void testExecute() throws Exception {
        final Commandline command = Mockito.mock(Commandline.class);
        
        when(builder.buildProtoCommand(any(File.class), any(Map.class), anyString())).thenReturn(command);
        
        mojo.execute();
        verify(command, times(1)).execute();
    }
    
    @Test (expected = MojoExecutionException.class) @SuppressWarnings("unchecked")
    public void testExecuteCommandLineExceptionGetsLaundered() throws Exception {
        final Commandline command = Mockito.mock(Commandline.class);
     
        when(builder.buildProtoCommand(any(File.class), any(Map.class), anyString())).thenReturn(command);
        when(command.execute()).thenThrow(new CommandLineException("Mock Exception"));
        
        mojo.execute();
    }
    
    @Test @SuppressWarnings("unchecked")
    public void testExecuteJavaOutputTypeAddsCompileSources() throws Exception {
        mojo.setOutputTypes(Collections.singletonMap("JAVA", "."));
        final Commandline command = Mockito.mock(Commandline.class);
        
        when(builder.buildProtoCommand(any(File.class), any(Map.class), anyString())).thenReturn(command);
        
        mojo.execute();
        verify(project, times(1)).addCompileSourceRoot(eq(new File(".").getAbsolutePath()));
    }
    
    @Test @SuppressWarnings("unchecked")
    public void testExecuteNoJavaOutputDoesNotAddCompileSources() throws Exception {
        mojo.setOutputTypes(Collections.singletonMap("PYTHON", "."));
        final Commandline command = Mockito.mock(Commandline.class);
        
        when(builder.buildProtoCommand(any(File.class), any(Map.class), anyString())).thenReturn(command);
        
        mojo.execute();        
        verify(project, times(0)).addCompileSourceRoot(anyString());
    }
    
    @Test (expected = MojoExecutionException.class)
    public void testExecuteEmptyExecutableThrowsMojoException() throws Exception {
        setField(mojo, "executable", StringUtils.EMPTY);
        mojo.execute();
    }
    
    @Test (expected = MojoExecutionException.class)
    public void testExecuteNullSourceDirectoryThrowsMojoException() throws Exception {
        setField(mojo, "sourceDirectory", null);
        mojo.execute();
    }

    @Test (expected = MojoExecutionException.class)
    public void testExecuteSourceDirectoryNotDirectoryThrowsMojoException() throws Exception {
        when(sourceDirectory.isDirectory()).thenReturn(false);
        mojo.execute();
    }

    @Test (expected = MojoExecutionException.class)
    public void testExecuteEmptyOutputTypesThrowsMojoException() throws Exception {
        setField(mojo, "executable", "test-executable");
        setField(mojo, "sourceDirectory", sourceDirectory);

        final Map<String, String> outputTypes = Collections.emptyMap();
        
        mojo.setOutputTypes(outputTypes);
        mojo.execute();
    }
    
    @Test (expected = MojoExecutionException.class)
    public void testExecuteNullOutputTypesThrowsMojoException() throws Exception {
        setField(mojo, "executable", "test-executable");
        setField(mojo, "sourceDirectory", sourceDirectory);

        mojo.setOutputTypes(null);
    }

    
    private static void setField(final Object target, final String fieldName, final Object value)
            throws IllegalAccessException, NoSuchFieldException {

        final Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}