package com.rhaggarty.protobuf;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;


/**
 * Non-test entry point for maven-protoc-plugin.
 * 
 * @goal compile
 * @phase generate-sources
 *
 * @author Ryan Haggarty (ryanmh at gmail dot com)
 */
public class ProtocolBufferMojo extends AbstractMojo {

    private static final String[] PROTO_FILE_EXTENSIONS = new String[] {"proto"};
    
    /** @parameter default-value="/usr/local/bin/protoc" **/
    private String executable;

    /** @parameter default-value="." **/
    private File outputDirectory;

    /** @parameter default-value="." **/
    private File sourceDirectory;

    /** @parameter property="outputTypes" **/
    private ProtoOutputType[] _outputTypes;

    
    public void execute() throws MojoExecutionException {
        mavenFriendlyValidateArgs();

        final Collection<File> files = findFiles();
        //outputDirectory.mkdirs();
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

    private void mavenFriendlyValidateArgs() throws MojoExecutionException {
        try {
            validateArgs();
        } catch (final IllegalArgumentException ex) {
            throw new MojoExecutionException(ex.getMessage().toUpperCase());
        }
    }

    private void validateArgs() {
        Validate.notNull(outputDirectory, "Output directory not found");
        Validate.notNull(sourceDirectory, "Source directory not found");
        Validate.notEmpty(executable, "protoc executable required");
        Validate.notEmpty(_outputTypes, "Output types cannot be empty");

        Validate.isTrue(!outputDirectory.isFile(), "Specified output directory is file!");
        Validate.isTrue(sourceDirectory.isDirectory(), "Specified source directory not directory!");
    }

    private Collection<File> findFiles() {
        return Collections.unmodifiableCollection(FileUtils.listFiles(sourceDirectory, PROTO_FILE_EXTENSIONS, true));
    }
}