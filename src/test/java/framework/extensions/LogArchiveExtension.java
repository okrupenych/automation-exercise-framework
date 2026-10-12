package framework.extensions;

import framework.utils.LogArchiveManager;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;

public class LogArchiveExtension implements BeforeAllCallback {

    private static final ExtensionContext.Namespace NAMESPACE =
            ExtensionContext.Namespace.create(LogArchiveExtension.class);

    @Override
    public void beforeAll(ExtensionContext context) {

        context.getRoot()
                .getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        LogArchiveExtension.class,
                        key -> {
                            try {
                                LogArchiveManager.archiveOldLogs();
                                LogArchiveManager.cleanupArchives();
                            } catch (IOException e) {
                                throw new IllegalStateException(
                                        "Failed to maintain log archives", e);
                            }

                            return Boolean.TRUE;
                        }
                );
    }
}