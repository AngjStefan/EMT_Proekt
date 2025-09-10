package backend.langchain4j;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  This is a solution for a bug, where the model will call the same tool 'findAllProductsByName' in a cycle
 *  where for some reason even though the correct products are found on the first call, it recalls itself with the prompt 'млеко'.
 *
 *  There is a hallucination or tool issue which I'm  trying to fix.
 */
@Component
public class ToolCallGuard {
    private final ThreadLocal<AtomicBoolean> toolCalled =
            ThreadLocal.withInitial(() -> new AtomicBoolean(false));

    public AtomicBoolean get() {
        return toolCalled.get();
    }

    public void reset() {
        toolCalled.set(new AtomicBoolean(false));
    }

    public void clear() {
        toolCalled.remove();
    }
}
