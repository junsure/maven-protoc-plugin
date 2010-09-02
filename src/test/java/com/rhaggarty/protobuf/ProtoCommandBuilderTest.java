package com.rhaggarty.protobuf;

import java.io.File;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class ProtoCommandBuilderTest {

    private static final String EXECUTABLE = "./this/is/not/a/real/executable";
    
    private Map<ProtoOutputType, String> outputTypes;
    
    @Mock private File sourceDirectory;
    
    private ProtoCommandBuilder builder;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this.getClass());
        builder = new ProtoCommandBuilder();
    }
    
    @Test
    public void testBuildProtoCommand() {
        //builder.buildProtoCommand(sourceDirectory, outputTypes, EXECUTABLE);
        
    }   
}