package MotherFocaTagTool.logger.factories;

import MotherFocaTagTool.logger.ILogger;

/**
 * Static factory for logger usage. Holds in memory a logger factory to
 * create statically under the demand the logger and use it from anywhere in
 * the application.
 * 
 * Here's an example of usage:
 * 
 * LoggerFactory.createLogger().logWarning("this is a warning message");
 * 
 */
public class LoggerFactory {

    private static ILoggerFactory _loggerFactory;

    /**
     * Creates a logger instance.
     * @return The logger instance.
     */
    public static ILogger createLogger()
    {
        return _loggerFactory.create();
    }
    
    /**
     * Sets the logger factory.
     * @param loggerFactory The logger factory to use.
     */
    public static void setLoggeFactory(ILoggerFactory loggerFactory)
    {
        if(loggerFactory == null)
        {
            throw new IllegalArgumentException("loggerFactory");
        }
        _loggerFactory = loggerFactory;
    }
    
}
