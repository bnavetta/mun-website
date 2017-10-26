package org.brownmun.util;

import java.util.function.Function;

/**
 * Helper functional interface for lambdas that throw exceptions. Any thrown checked exceptions will
 * be wrapped in a {@link RuntimeException}.
 */
@FunctionalInterface
public interface ExceptionalFunction<T, R, E extends Throwable> extends Function<T, R>
{
    R throwingApply(T arg) throws E;

    default R apply(T arg)
    {
        try
        {
            return throwingApply(arg);
        }
        catch (RuntimeException | Error e)
        {
            throw e;
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Casting wrapper for passing {@code ExceptionalFunction}s as regular {@link Function}s.
     */
    static <T, R, E extends Throwable> Function<T, R> wrap(ExceptionalFunction<T, R, E> fun)
    {
        return fun;
    }
}
