package MotherFocaTagTool.logger.factories;

import MotherFocaTagTool.logger.ILogger;
import MotherFocaTagTool.logger.InMemoryLogger;

/**
 * Factory class for InMemory logger creation.
 */
public class InMemoryLoggerFactory implements ILoggerFactory {

    /**
     * Creates a new logger InMemoryLogger instance.
     * @return The created instance.
     */
    @Override
    public ILogger create() {
        return new InMemoryLogger();
    }
    
}
