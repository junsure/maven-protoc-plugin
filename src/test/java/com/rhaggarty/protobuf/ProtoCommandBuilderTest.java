package com.rhaggarty.protobuf;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileFilter;
import java.util.Collections;
import java.util.Map;

import org.codehaus.plexus.util.cli.Commandline;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


public class ProtoCommandBuilderTest {

    private static final String EXECUTABLE = "./this/is/not/a/real/executable";
    
    @Mock private File sourceDirectory;
    
    private ProtoCommandBuilder builder;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        builder = new ProtoCommandBuilder();
    }
    
    @Test
    public void testBuildProtoCommand() {
        final Map<ProtoOutputType, String> outputTypes = Collections.singletonMap(ProtoOutputType.JAVA, ".");
        final File mockFile = Mockito.mock(File.class);
        
        when(sourceDirectory.isDirectory()).thenReturn(true);
        when(sourceDirectory.listFiles(any(FileFilter.class))).thenReturn(new File[] {mockFile});
        when(sourceDirectory.toString()).thenReturn(".");
        when(mockFile.toString()).thenReturn("mock-file.proto");
        
        final Commandline command = builder.buildProtoCommand(sourceDirectory, outputTypes, EXECUTABLE);
        
        final StringBuilder sb = new StringBuilder("/bin/sh -c ");
        sb.append(EXECUTABLE).append(" ").append(mockFile).append(" ");
        sb.append("--java_out=. --proto_path=.");
         
        Assert.assertEquals(sb.toString(), command.toString());
        System.out.println(command);
    }   
}