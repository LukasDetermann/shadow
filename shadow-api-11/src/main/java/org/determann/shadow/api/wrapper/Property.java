package org.determann.shadow.api.wrapper;

import org.determann.shadow.api.ApiHolder;
import org.determann.shadow.api.shadow.Field;
import org.determann.shadow.api.shadow.Method;

import java.util.Optional;

/**
 * There are 2 types of properties. they both share:
 * <ul>
 *    <li>the field is not static</li>
 *    <li>a method with the name "is" or "get" + {@code shadowApi.to_UpperCamelCase(field.getSimpleName())} as the getter</li>
 * </ul>
 *
 * <h4>Mutable Property</h4>
 * <ul>
 *    <li>the field is not final</li>
 *    <li>a method with the name "set" + {@code shadowApi.to_UpperCamelCase(field.getSimpleName())} as the setter</li>
 * </ul>
 *
 * <h4>Mutable Property</h4>
 * <ul>
 *    <li>the field is final</li>
 *    <li>no setter</li>
 * </ul>
 */
public interface Property extends ApiHolder
{
   Field getField();

   Method getGetter();

   Method getSetterOrThrow();

   Optional<Method> getSetter();

   boolean isMutable();
}
