/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.project.build.libs;

import static com.google.common.collect.Lists.newArrayList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.ui.statushandlers.StatusManager;
import org.rf.ide.core.executor.RobotRuntimeEnvironment;
import org.rf.ide.core.executor.RobotRuntimeEnvironment.RobotEnvironmentException;
import org.rf.ide.core.executor.SuiteExecutor;
import org.rf.ide.core.libraries.LibraryDescriptor;
import org.rf.ide.core.libraries.LibrarySpecification;
import org.rf.ide.core.project.RobotProjectConfig;
import org.rf.ide.core.project.RobotProjectConfig.LibraryType;
import org.rf.ide.core.project.RobotProjectConfig.RemoteLocation;
import org.robotframework.ide.eclipse.main.plugin.RedPlugin;
import org.robotframework.ide.eclipse.main.plugin.RedWorkspace;
import org.robotframework.ide.eclipse.main.plugin.model.LibspecsFolder;
import org.robotframework.ide.eclipse.main.plugin.model.RobotProject;
import org.robotframework.ide.eclipse.main.plugin.project.RedEclipseProjectConfig;
import org.robotframework.ide.eclipse.main.plugin.project.build.BuildLogger;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

public class LibrariesBuilder {

    private final BuildLogger logger;

    public LibrariesBuilder(final BuildLogger logger) {
        this.logger = logger;
    }

    public void forceLibrariesRebuild(final Multimap<IProject, LibrarySpecification> groupedSpecifications,
            final SubMonitor monitor) {
        monitor.subTask("generating libdocs");

        final Multimap<IProject, ILibdocGenerator> groupedGenerators = LinkedHashMultimap.create();
        groupedSpecifications.forEach((project, spec) -> {
            final String fileName = spec.getDescriptor().generateLibspecFileName();
            final IFile xmlTargetFile = LibspecsFolder.get(project).getXmlSpecFile(fileName);

            groupedGenerators.put(project, provideGenerator(spec.getDescriptor(), xmlTargetFile));
        });

        monitor.setWorkRemaining(groupedGenerators.size());
        for (final IProject project : groupedGenerators.keySet()) {
            final RobotProject robotProject = RedPlugin.getModelManager().createProject(project);
            final RobotRuntimeEnvironment runtimeEnvironment = robotProject.getRuntimeEnvironment();

            MultiStatus multiStatus = null;
            for (final ILibdocGenerator generator : groupedGenerators.get(project)) {
                monitor.subTask(generator.getMessage());
                try {
                    if (project.exists()) {
                        generator.generateLibdocForcibly(runtimeEnvironment,
                                new RedEclipseProjectConfig(robotProject.getRobotProjectConfig())
                                        .createEnvironmentSearchPaths(project));
                    }
                } catch (final RobotEnvironmentException e) {
                    final Status status = new Status(IStatus.ERROR, RedPlugin.PLUGIN_ID,
                            "\nProblem occurred during " + generator.getMessage() + ".", e);
                    if (multiStatus == null) {
                        multiStatus = new MultiStatus(RedPlugin.PLUGIN_ID, IStatus.ERROR, new Status[] { status },
                                "Library specification generation problem", null);
                    } else {
                        multiStatus.add(status);
                    }

                    try {
                        generator.getTargetFile().delete(true, new NullProgressMonitor());
                    } catch (final CoreException e1) {
                        multiStatus.add(e1.getStatus());
                    }
                }
                monitor.worked(1);
            }

            if (multiStatus != null) {
                StatusManager.getManager().handle(multiStatus, StatusManager.BLOCK);
            }
        }
        monitor.done();
    }

    private ILibdocGenerator provideGenerator(final LibraryDescriptor libraryDescriptor, final IFile targetFile) {
        final String nameForLibdoc = createLibraryName(libraryDescriptor);

        if (libraryDescriptor.isStandardLibrary()) {
            return new StandardLibraryLibdocGenerator(nameForLibdoc, targetFile);

        } else {
            final String path = libraryDescriptor.getPath();

            final LibraryType type = libraryDescriptor.getLibraryType();
            if (type == LibraryType.VIRTUAL) {
                return new VirtualLibraryLibdocGenerator(Path.fromPortableString(path), targetFile);

            } else if (type == LibraryType.PYTHON) {
                return new PythonLibraryLibdocGenerator(nameForLibdoc, toAbsolute(path), targetFile);

            } else if (type == LibraryType.JAVA) {
                return new JavaLibraryLibdocGenerator(nameForLibdoc, toAbsolute(path), targetFile);
            }
            throw new IllegalStateException("Unknown library type: " + type);
        }
    }

    private static String createLibraryName(final LibraryDescriptor libraryDescriptor) {
        // libdoc tool requires arguments to be provided with name: Lib::arg1::arg2
        final List<String> nameToGenerate = newArrayList(libraryDescriptor.getName());
        nameToGenerate.addAll(libraryDescriptor.getArguments());
        return String.join("::", nameToGenerate);
    }

    public void buildLibraries(final RobotProject robotProject, final RobotRuntimeEnvironment environment,
            final RobotProjectConfig configuration, final SubMonitor monitor) {
        logger.log("BUILDING: generating library docs");
        monitor.subTask("generating libdocs");

        final List<ILibdocGenerator> libdocGenerators = new ArrayList<>();

        final LibspecsFolder libspecsFolder = LibspecsFolder.get(robotProject.getProject());
        libdocGenerators.addAll(getStandardLibrariesToRecreate(environment, libspecsFolder));
        libdocGenerators.addAll(getStandardRemoteLibrariesToRecreate(configuration, libspecsFolder));
        libdocGenerators.addAll(getReferencedVirtualLibrariesToRecreate(configuration, libspecsFolder));
        libdocGenerators.addAll(getReferencedPythonLibrariesToRecreate(configuration, libspecsFolder));
        if (environment.getInterpreter() == SuiteExecutor.Jython) {
            libdocGenerators.addAll(getReferencedJavaLibrariesToRecreate(configuration, libspecsFolder));
        }

        monitor.setWorkRemaining(libdocGenerators.size());

        for (final ILibdocGenerator generator : libdocGenerators) {
            if (monitor.isCanceled()) {
                return;
            }

            logger.log("BUILDING: " + generator.getMessage());
            monitor.subTask(generator.getMessage());
            try {
                generator.generateLibdoc(environment, new RedEclipseProjectConfig(configuration)
                        .createEnvironmentSearchPaths(robotProject.getProject()));
            } catch (final RobotEnvironmentException e) {
                // the libraries with missing libspec are reported in validation phase
            }
            monitor.worked(1);
        }

        monitor.done();
    }

    private List<ILibdocGenerator> getStandardLibrariesToRecreate(final RobotRuntimeEnvironment environment,
            final LibspecsFolder libspecsFolder) {
        final List<ILibdocGenerator> generators = new ArrayList<>();

        for (final String stdLib : environment.getStandardLibrariesNames()) {
            final String fileName = LibraryDescriptor.ofStandardLibrary(stdLib).generateLibspecFileName();

            final IFile xmlSpecFile = libspecsFolder.getXmlSpecFile(fileName);
            if (!fileExist(xmlSpecFile)
                    || !hasSameVersion(new File(xmlSpecFile.getLocationURI()), environment.getVersion())) {
                // we always want to regenerate standard libraries when RF version have changed
                // or libdoc does not exist
                generators.add(new StandardLibraryLibdocGenerator(stdLib, xmlSpecFile));
            }
        }
        return generators;
    }

    private static boolean hasSameVersion(final File file, final String version) {
        return version.startsWith(String.format("Robot Framework %s (", LibrarySpecification.getVersion(file)));
    }

    private List<ILibdocGenerator> getStandardRemoteLibrariesToRecreate(final RobotProjectConfig configuration,
            final LibspecsFolder libspecsFolder) {

        final List<ILibdocGenerator> generators = new ArrayList<>();

        for (final RemoteLocation location : configuration.getRemoteLocations()) {
            final String fileName = LibraryDescriptor.ofStandardRemoteLibrary(location).generateLibspecFileName();

            // we always want to regenerate remote libraries, as something may have changed
            final IFile xmlSpecFile = libspecsFolder.getXmlSpecFile(fileName);
            generators.add(new StandardLibraryLibdocGenerator("Remote::" + location.getUri(), xmlSpecFile));
        }
        return generators;
    }

    private List<ILibdocGenerator> getReferencedVirtualLibrariesToRecreate(final RobotProjectConfig configuration,
            final LibspecsFolder libspecsFolder) {
        final List<ILibdocGenerator> generators = new ArrayList<>();

        configuration.getLibraries().stream().filter(lib -> lib.provideType() == LibraryType.VIRTUAL).forEach(lib -> {
            final Path libPath = new Path(lib.getPath());

            // we only copy workspace-external specs to libspecs folder; those in workspace should
            // be read directly
            if (libPath.isAbsolute()) {
                final String fileName = LibraryDescriptor.ofReferencedLibrary(lib).generateLibspecFileName();

                final IFile xmlSpecFile = libspecsFolder.getXmlSpecFile(fileName);
                if (!fileExist(xmlSpecFile)) {
                    generators.add(new VirtualLibraryLibdocGenerator(libPath, xmlSpecFile));
                }
            }
        });
        return generators;
    }

    private List<ILibdocGenerator> getReferencedPythonLibrariesToRecreate(final RobotProjectConfig configuration,
            final LibspecsFolder libspecsFolder) {
        final List<ILibdocGenerator> generators = new ArrayList<>();

        configuration.getLibraries().stream().filter(lib -> lib.provideType() == LibraryType.PYTHON).forEach(lib -> {
            final String fileName = LibraryDescriptor.ofReferencedLibrary(lib).generateLibspecFileName();

            final IFile xmlSpecFile = libspecsFolder.getXmlSpecFile(fileName);
            if (!fileExist(xmlSpecFile)) {
                generators.add(new PythonLibraryLibdocGenerator(lib.getName(), toAbsolute(lib.getPath()), xmlSpecFile));
            }
        });
        return generators;
    }

    private List<ILibdocGenerator> getReferencedJavaLibrariesToRecreate(final RobotProjectConfig configuration,
            final LibspecsFolder libspecsFolder) {
        final List<ILibdocGenerator> generators = new ArrayList<>();

        configuration.getLibraries().stream().filter(lib -> lib.provideType() == LibraryType.JAVA).forEach(lib -> {
            final String fileName = LibraryDescriptor.ofReferencedLibrary(lib).generateLibspecFileName();

            final IFile xmlSpecFile = libspecsFolder.getXmlSpecFile(fileName);
            if (!fileExist(xmlSpecFile)) {
                generators.add(new JavaLibraryLibdocGenerator(lib.getName(), toAbsolute(lib.getPath()), xmlSpecFile));
            }
        });
        return generators;
    }

    private static boolean fileExist(final IFile file) {
        return file.exists() && file.getLocation().toFile().exists();
    }

    private static String toAbsolute(final String path) {
        return RedWorkspace.Paths.toAbsoluteFromWorkspaceRelativeIfPossible(Path.fromPortableString(path)).toOSString();
    }
}
