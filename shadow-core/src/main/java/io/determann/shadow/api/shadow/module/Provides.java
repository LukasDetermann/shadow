package io.determann.shadow.api.shadow.module;

import io.determann.shadow.api.shadow.type.Declared;

import java.util.List;

/**
 * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
 *
 * @see Uses
 */
public interface Provides extends Directive
{
   /**
    * a service to provide to other modules
    */
   Declared getService();

   /**
    * Implementations of the provided service
    */
   List<Declared> getImplementations();
}
