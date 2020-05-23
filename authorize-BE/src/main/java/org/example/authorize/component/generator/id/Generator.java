package org.example.authorize.component.generator.id;

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

    /**
     * Generating ID with value is determined.
     *
     * @param value the value to encrypt to id
     * @return return the ID after encrypt value
     */
    T generate(T value);
}
