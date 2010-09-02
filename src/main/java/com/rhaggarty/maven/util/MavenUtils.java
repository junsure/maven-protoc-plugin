package com.rhaggarty.maven.util;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * A simple suite of maven helper methods.
 * 
 * @author Ryan Haggarty (ryanmh@gmail.com)
 *
 */
public final class MavenUtils {

    private MavenUtils() {
        throw new AssertionError("This class cannot be instantiated");
    }
    
    /**
     * A simple validation routine that throws a Maven-friendly MovoExecutionException on failure.  This can be considered
     * a maven-specific replacement for Validate.isTrue in Apache commons-lang.
     * 
     * @param expression The expression to evaluate
     * @param msg The message to to include in the thrown exception if the expression evaluates to false.
     * 
     * @throws MojoExecutionException if expression evaluates to false.
     */
    public static void validateIsTrue(final boolean expression, final String msg) throws MojoExecutionException {
        if (!expression) {
            throw new MojoExecutionException(msg);
        }
    }
}
