package org.example.authorize.generator;

/**
 * Generator interface.
 *
 * @param <T>
 */
public interface Generator<T> {

    /**
     * Generating value.
     *
     * @return return value is generated
     */
    T generate();
}
