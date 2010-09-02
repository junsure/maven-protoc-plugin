package com.rhaggarty.protobuf;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.codehaus.plexus.util.cli.Commandline;

/**
 * A simple builder for a protoc command.
 * 
 * @author rhaggarty
 *
 */
public class ProtoCommandBuilder {

    private static final String[] PROTO_FILE_EXTENSIONS = new String[] {"proto"};
    
    public Commandline buildProtoCommand(final File sourceDirectory, final Map<ProtoOutputType, String> outputTypes, final String executable) {
        final Collection<String> files = findFiles(sourceDirectory);
        final Collection<String> args = getArgs(sourceDirectory, outputTypes);
        
        final Commandline commandline = new Commandline(executable);
        commandline.addArguments(files.toArray(new String[] {}));
        commandline.addArguments(args.toArray(new String[] {}));

        return commandline;
    }
    
    private static Collection<String> findFiles(final File sourceDirectory) {
        @SuppressWarnings("unchecked") final Collection<File> files = FileUtils.listFiles(sourceDirectory, PROTO_FILE_EXTENSIONS, true);
        final Collection<String> filesAsStrings = new HashSet<String>(files.size());
        for (final File file : files) {
            filesAsStrings.add(file.toString());
        }
        return filesAsStrings;
    }


    private static Collection<String> getArgs(final File sourceDirectory, final Map<ProtoOutputType, String> outputTypes) {
        final Collection<String> args = new LinkedHashSet<String>();
        for (final Map.Entry<ProtoOutputType, String> outputType : outputTypes.entrySet()) {
            args.add(outputType.getKey().getArg() + "=" + outputType.getValue());
            final File file = new File(outputType.getValue());
            file.mkdirs();
        }
        args.add("--proto_path=" + sourceDirectory);

        return args;
    }
}
