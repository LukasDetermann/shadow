package io.determann.shadow.api.reflection.shadow.structure;

import io.determann.shadow.api.reflection.shadow.R_Nameable;
import io.determann.shadow.api.reflection.shadow.type.R_Type;
import io.determann.shadow.api.shadow.structure.C_Field;
import io.determann.shadow.api.shadow.structure.C_Property;

import java.util.Optional;

/// This represents a java beans Property. Only a [Getter][R_Property#getGetter()] is mandatory
///
/// [Getter][R_Property#getGetter()]
/// - not static
/// - the name starts with "get" and is longer then 3 or
/// the name starts with "is" and has the return type [Boolean] or [Boolean#TYPE] and is longer then 2
///   - if a "is" and a "get" setter are present "is" is preferred
///
/// [Setter][R_Property#getSetter()]
/// - not static
/// - the names has to match the getter name
/// - the name starts with "set" and is longer then 3
/// - has one parameter
/// - the parameter has the same type as the Getter
///
/// [Field][R_Property#getField()]
/// - not static
/// - has the same type as the Getter
/// - if the name of the Getter without prefix is longer then 1 and has an uppercase Character
/// at index 0 or 1 the field name has to match exactly
/// or the name matches with the first Character converted to lowercase
///    - [Java Beans 8.8](https://www.oracle.com/java/technologies/javase/javabeans-spec.html)
public interface R_Property

      extends C_Property,
              R_Nameable
{
   /**
    * based on the name of the getter without the prefix. if one of the first 2 chars is uppercase the
    * property name is not changed. otherwise the first char is converted to lowercase
    *
    * @see #getGetter()
    */
   String getName();

   /**
    * return type of getter
    *
    * @see #getGetter()
    */
   R_Type getType();

   /**
    * a {@link C_Field} with the name and tye of this property
    *
    * @see #getName()
    * @see #getType()
    */
   Optional<R_Field> getField();

   /**
    * @see #getField()
    */
   R_Field getFieldOrThrow();

   /**
    * 2 possible types of getters
    * <ul>
    * <li>return type boolean, name prefix "is" and no parameters</li>
    * <li>name prefix is "get" and no parameters</li>
    * </ul>
    * when both are present "is" is preferred over get
    */
   R_Method getGetter();

   /**
    * a method with the same name as the getter but "set" instead of "is" or "get", return type void and one
    * parameter being the type of the return of the getter
    *
    * @see #getGetter()
    */
   Optional<R_Method> getSetter();

   /**
    * @see #getSetter()
    */
   R_Method getSetterOrThrow();

   /**
    * has a {@link #getSetter()}
    */
   boolean isMutable();
}
