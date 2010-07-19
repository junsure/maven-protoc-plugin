package com.rhaggarty.protobuf;

import com.rhaggarty.protobuf.type.ProtoOutputType;
import java.io.File;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;


/**
 * Non-test entry point for maven-protoc-plugin.
 * 
 * @goal compile
 * @phase generate-sources
 *
 * @author Ryan Haggarty (ryanmh at gmail dot com)
 */
public class ProtocolBufferMojo extends AbstractMojo {

    /** @parameter default-value="/usr/local/bin/protoc" **/
    private String executable;

    /** @parameter default-value="." **/
    private File outputDirectory;

    /** @parameter default-value="." **/
    private File sourceDirectory;

    /** @parameter property="outputTypes" **/
    private ProtoOutputType[] _outputTypes;

    
    public void execute() throws MojoExecutionException {
        validateArgs();
    }

    /**
     * @param outputTypes a String-based array representing one or more
     * {@link ProtoOutputType)s.
     *
     * @throws IllegalArgumentException if any of the specified strings doesn't
     * map to a ProtoOutputType.
     */
    public void setOutputTypes(final String[] outputTypes) {
        _outputTypes = ProtoOutputType.valuesOf(outputTypes);
    }

    private void validateArgs() {
        Validate.notNull(outputDirectory);
        Validate.notNull(sourceDirectory);
        Validate.notEmpty(executable);
        Validate.notEmpty(_outputTypes);

        Validate.isTrue(outputDirectory.isDirectory());
        Validate.isTrue(sourceDirectory.isDirectory());
    }
}