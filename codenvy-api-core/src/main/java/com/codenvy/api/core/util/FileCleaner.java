/*******************************************************************************
 * Copyright (c) 2012-2015 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package com.codenvy.api.core.util;

import com.codenvy.commons.lang.IoUtil;
import com.codenvy.commons.lang.NamedThreadFactory;
import com.codenvy.commons.lang.Pair;
import com.codenvy.inject.DynaModule;
import com.google.inject.AbstractModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Delete files and directories added with method {@link @addFile}. In environments with multiple class loaders, {@code FileCleaner} should
 * be stopped if it isn't needed. In Codenvy environment {@code FileCleaner} is stopped automatically by {@link FileCleanerModule}
 * otherwise
 * need call method {@link #stop()} manually.
 *
 * @author andrew00x
 */
public class FileCleaner {
    private static final Logger LOG = LoggerFactory.getLogger(FileCleaner.class);

    /** Number of attempts to delete file or directory before start write log error messages. */
    static int logAfterAttempts = 10;

    private static ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("FileCleaner", true));

    static {
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                clean();
            }
        }, 30, 30, TimeUnit.SECONDS);
    }

    private static ConcurrentLinkedQueue<Pair<File, Integer>> files = new ConcurrentLinkedQueue<>();

    /** Registers new file or directory in FileCleaner. */
    public static void addFile(File file) {
        files.offer(Pair.of(file, 0));
    }

    /** Stops FileCleaner. */
    public static void stop() {
        exec.shutdownNow();
        try {
            exec.awaitTermination(3, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        clean();
        files.clear();
        LOG.info("File cleaner is stopped");
    }

    private static void clean() {
        Pair<File, Integer> pair;
        final Set<Pair<File, Integer>> failToDelete = new HashSet<>();
        while ((pair = files.poll()) != null) {
            final File file = pair.first;
            int deleteAttempts = pair.second;
            if (file.exists()) {
                if (!IoUtil.deleteRecursive(file)) {
                    failToDelete.add(Pair.of(file, ++deleteAttempts));
                    if (deleteAttempts > logAfterAttempts) {
                        LOG.error("Unable delete file '{}' after {} tries", file, deleteAttempts);
                    }
                } else if (LOG.isDebugEnabled()) {
                    LOG.debug("Delete file '{}'", file);
                }
            }
        }
        if (!failToDelete.isEmpty()) {
            files.addAll(failToDelete);
        }
    }

    /** Guice module that stops FileCleaner when Guice container destroyed. */
    @DynaModule
    public static class FileCleanerModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Finalizer.class).asEagerSingleton();
        }
    }

    /** Helper component that stops FileCleaner. */
    static class Finalizer {
        @PreDestroy
        void stop() {
            FileCleaner.stop();
        }
    }

    private FileCleaner() {
    }
}
