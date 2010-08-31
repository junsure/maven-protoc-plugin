package com.rhaggarty.protobuf;

import java.io.File;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * Non-test entry point for maven-protoc-plugin.
 * 
 * @goal compile
 * @phase generate-sources
 * @requiresDependencyResolution compile
 *
 * @author Ryan Haggarty (ryanmh at gmail dot com)
 */
public class ProtocolBufferMojo extends AbstractMojo {

    private static final String[] PROTO_FILE_EXTENSIONS = new String[] {"proto"};
    
    /** @parameter default-value="/usr/local/bin/protoc" **/
    private String executable;

    /** @parameter default-value="." **/
    private File sourceDirectory;

    /** @parameter property="outputTypes" **/
    private Map<ProtoOutputType, String> _outputTypes;

    /**
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
     private MavenProject project;

    
    public void execute() throws MojoExecutionException {
        mavenFriendlyValidateArgs();

        final Collection<String> files = findFiles();
        final Collection<String> args = getArgs();
        final Commandline commandline = new Commandline(executable);
        commandline.addArguments(files.toArray(new String[] {}));
        commandline.addArguments(args.toArray(new String[] {}));

        System.out.println("Didi Mau - " + commandline.toString());
        
        try {
            commandline.execute();
            if (_outputTypes.containsKey(ProtoOutputType.JAVA)) {
                project.addCompileSourceRoot(new File(_outputTypes.get(ProtoOutputType.JAVA)).getAbsolutePath());
            }
        } catch (final CommandLineException ex) {
            throw new MojoExecutionException("Caught CommandLine Exception", ex);
        }
        
        
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
        Validate.notNull(sourceDirectory, "Source directory not found");
        Validate.notEmpty(executable, "protoc executable required");
        Validate.notEmpty(_outputTypes, "Output types cannot be empty");

        Validate.isTrue(sourceDirectory.isDirectory(), "Specified source directory not directory!");
    }

    private Collection<String> findFiles() {
        @SuppressWarnings("unchecked") final Collection<File> files = FileUtils.listFiles(sourceDirectory, PROTO_FILE_EXTENSIONS, true);
        final Collection<String> filesAsStrings = new HashSet<String>(files.size());
        for (final File file : files) {
            filesAsStrings.add(file.toString());
        }
        return filesAsStrings;
    }

    private Collection<String> getArgs() {
        final Collection<String> args = new LinkedHashSet<String>();
        for (final Map.Entry<ProtoOutputType, String> outputType : _outputTypes.entrySet()) {
            args.add(outputType.getKey().getArg() + "=" + outputType.getValue());
            final File file = new File(outputType.getValue());
            file.mkdirs();
        }
        args.add("--proto_path=" + sourceDirectory);

        return args;
    }
}