package com.rhaggarty.protobuf;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.cli.Commandline;

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
    private Map<ProtoOutputType, String> _outputTypes;

    
    public void execute() throws MojoExecutionException {
        mavenFriendlyValidateArgs();

        final Collection<String> files = findFiles();
        final Collection<String> args = getArgs();
        final Commandline commandline = new Commandline(executable);
        commandline.addArguments(files.toArray(new String[] {}));
        commandline.addArguments(args.toArray(new String[] {}));
        System.out.println(commandline.toString());
        
        //outputDirectory.mkdirs();
    }

    /**
     * @param outputTypes a String-based array representing one or more
     * {@link ProtoOutputType)s.
     *
     * @throws IllegalArgumentException if any of the specified strings doesn't
     * map to a ProtoOutputType.
     */
    public void setOutputTypes(final Map<String, String> outputTypes) {
        _outputTypes = new EnumMap<ProtoOutputType, String>(ProtoOutputType.class);
        for (final Map.Entry<String, String> entry : outputTypes.entrySet()) {
            _outputTypes.put(ProtoOutputType.valueOf(entry.getKey()), entry.getValue());
        }
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

    private Collection<String> findFiles() {
        final Collection<File> files = FileUtils.listFiles(sourceDirectory, PROTO_FILE_EXTENSIONS, true);
        final Collection<String> filesAsStrings = new HashSet<String>(files.size());
        for (final File file : files) {
            filesAsStrings.add(file.toString());
        }
        return filesAsStrings;
    }

    private Collection<String> getArgs() {
        final Collection<String> args = new LinkedHashSet<String>();
        for (final Map.Entry<ProtoOutputType, String> outputType : _outputTypes.entrySet()) {
            args.add(outputType.getKey() + "=" + outputType.getValue());
        }

        return args;
    }
}