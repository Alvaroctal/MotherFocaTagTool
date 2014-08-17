package MotherFocaTagTool.logger;

/**
 * Base contract for all logger implementations.
 */
public interface ILogger {
    
    /**
     * Logs a warning.
     * @param message The message to log.
     */
    public void logWarning(String message);
    
    /**
     * Logs an error.
     * @param message The message to log. 
     */
    public void logError(String message);
    
    /**
     * Logs an informative message.
     * @param message The message to log.
     */
    public void logInfo(String message);
}
