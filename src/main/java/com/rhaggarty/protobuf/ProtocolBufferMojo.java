package com.rhaggarty.protobuf;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.Commandline;

import com.rhaggarty.maven.util.MavenUtils;

/**
 * Entry point for maven-protoc-plugin.
 * 
 * @goal compile
 * @phase generate-sources
 * @requiresDependencyResolution compile
 * 
 * @author Ryan Haggarty (ryanmh@gmail.com)
 */
public final class ProtocolBufferMojo extends AbstractMojo {

    /**
     * @parameter default-value="/usr/local/bin/protoc"
     */
    private String executable;

    /**
     * @parameter default-value="."
     */
    private File sourceDirectory;

    /**
     * @parameter property="outputTypes"
     * @required
     */
    private Map<ProtoOutputType, String> _outputTypes;

    /**
     * @parameter default-value="${project}"
     * @readonly
     * @required
     */
    private MavenProject project;

    private final ProtoCommandBuilder builder = new ProtoCommandBuilder();

    
    public void execute() throws MojoExecutionException {
        validateArgs();

        final Commandline command = builder.buildProtoCommand(sourceDirectory, _outputTypes, executable);
        System.out.println("Didi Mau - " + command.toString());

        try {
            command.execute();
            if (_outputTypes.containsKey(ProtoOutputType.JAVA)) {
                project.addCompileSourceRoot(new File(_outputTypes.get(ProtoOutputType.JAVA)).getAbsolutePath());
            }
        } catch (final CommandLineException ex) {
            throw new MojoExecutionException("Caught CommandLine Exception", ex);
        }
    }

    /**
     * @param outputTypes a String-based array representing one or more {@link ProtoOutputType)s.
     * 
     * @throws IllegalArgumentException if any of the specified strings doesn't map to a ProtoOutputType.
     */
    public void setOutputTypes(final Map<String, String> outputTypes) throws MojoExecutionException {
        MavenUtils.validateIsTrue(outputTypes != null, "Output types cannot be null");

        _outputTypes = new EnumMap<ProtoOutputType, String>(ProtoOutputType.class);
        for (final Map.Entry<String, String> entry : outputTypes.entrySet()) {
            _outputTypes.put(ProtoOutputType.valueOf(entry.getKey()), entry.getValue());
        }
    }

    private void validateArgs() throws MojoExecutionException {
        MavenUtils.validateIsTrue(sourceDirectory != null && sourceDirectory.isDirectory(),
                "Source directory not found or not a directory");
        MavenUtils.validateIsTrue(StringUtils.isNotEmpty(executable), "protoc executable required");
        MavenUtils.validateIsTrue(!_outputTypes.isEmpty(), "Output types cannot be empty");
    }
}