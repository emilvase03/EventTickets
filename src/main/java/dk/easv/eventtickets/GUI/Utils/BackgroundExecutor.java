package dk.easv.eventtickets.GUI.Utils;

// Java imports
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

// JavaFX imports
import javafx.concurrent.Task;

public class BackgroundExecutor {

    private static final ExecutorService executor = Executors.newCachedThreadPool(r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        return t;
    });

    /**
     * Run a background task with success, error, and loading callbacks.
     *
     * @param backgroundWork  the work to run off the FX thread
     * @param onSuccess       called on the FX thread when work completes
     * @param onError         called on the FX thread if work throws
     * @param onLoadingChange called with true when starting, false when done
     */
    public static <T> void execute(
            Callable<T> backgroundWork,
            Consumer<T> onSuccess,
            Consumer<Exception> onError,
            Consumer<Boolean> onLoadingChange) {

        Task<T> task = new Task<>() {
            @Override
            protected T call() throws Exception {
                return backgroundWork.call();
            }
        };

        task.setOnRunning(e -> {
            if (onLoadingChange != null)
                onLoadingChange.accept(true);
        });

        task.setOnSucceeded(e -> {
            if (onLoadingChange != null)
                onLoadingChange.accept(false);
            if (onSuccess != null)
                onSuccess.accept(task.getValue());
        });

        task.setOnFailed(e -> {
            if (onLoadingChange != null)
                onLoadingChange.accept(false);
            if (onError != null)
                onError.accept((Exception) task.getException());
        });

        executor.submit(task);
    }

    /**
     * Overload without a loading callback.
     */
    public static <T> void execute(
            Callable<T> backgroundWork,
            Consumer<T> onSuccess,
            Consumer<Exception> onError) {
        execute(backgroundWork, onSuccess, onError, null);
    }

    /**
     * Overload for fire-and-forget tasks that return nothing.
     */
    public static void execute(
            Runnable backgroundWork,
            Runnable onSuccess,
            Consumer<Exception> onError) {
        execute(
                () -> { backgroundWork.run(); return null; },
                result -> { if (onSuccess != null) onSuccess.run(); },
                onError,
                null
        );
    }
}