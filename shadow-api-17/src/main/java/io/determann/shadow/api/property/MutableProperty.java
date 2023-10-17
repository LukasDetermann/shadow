package io.determann.shadow.api.property;

import io.determann.shadow.api.shadow.Field;
import io.determann.shadow.api.shadow.Method;
import io.determann.shadow.api.shadow.Shadow;

import java.util.Optional;

/**
 * A stricter definition of a {@link Property} where getter and setter are mandatory
 *
 * @see Property
 * @see ImmutableProperty
 */
public interface MutableProperty
{
   /**
    * based on the name of the getter without the prefix. if one of the first 2 chars is uppercase the
    * property name is not changed. otherwise the first char is converted to lowercase
    *
    * @see #getGetter()
    */
   String getSimpleName();

   /**
    * return type of getter
    *
    * @see #getGetter()
    */
   Shadow getType();

   /**
    * a {@link Field} with the name and tye of this property
    *
    * @see #getSimpleName()
    * @see #getType()
    */
   Optional<Field> getField();

   /**
    * @see #getField()
    */
   Field getFieldOrThrow();

   /**
    * 2 possible types of getters
    * <ul>
    * <li>return type boolean, name prefix "is" and no parameters</li>
    * <li>name prefix is "get" and no parameters</li>
    * </ul>
    * when both are present "is" is preferred over get
    */
   Method getGetter();

   /**
    * a method with the same name as the getter but "set" instead of "is" or "get", return type void and one
    * parameter being the type of the return of the getter
    *
    * @see #getGetter()
    */
   Method getSetter();
}
