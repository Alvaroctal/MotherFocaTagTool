package MotherFocaTagTool.logger.factories;

import MotherFocaTagTool.logger.ILogger;

/**
 * Base contract for all logger factories.
 */
public interface ILoggerFactory {
    
    /**
     * Creates a logger instance.
     * @return 
     */
    ILogger create();
    
}
