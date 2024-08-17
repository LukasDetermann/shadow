package io.determann.shadow.api.lang_model.shadow.structure;

import io.determann.shadow.api.lang_model.shadow.type.ShadowLangModel;
import io.determann.shadow.api.shadow.structure.Field;
import io.determann.shadow.api.shadow.structure.Property;

import java.util.Optional;

/**
 * This represents the broadest definition of a property. only a {@link #getGetter()} is mandatory
 * <p>
 * The search for properties starts with the getter {@link #getGetter()}
 */
public interface PropertyLangModel extends Property
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
   ShadowLangModel getType();

   /**
    * a {@link Field} with the name and tye of this property
    *
    * @see #getName()
    * @see #getType()
    */
   Optional<FieldLangModel> getField();

   /**
    * @see #getField()
    */
   FieldLangModel getFieldOrThrow();

   /**
    * 2 possible types of getters
    * <ul>
    * <li>return type boolean, name prefix "is" and no parameters</li>
    * <li>name prefix is "get" and no parameters</li>
    * </ul>
    * when both are present "is" is preferred over get
    */
   MethodLangModel getGetter();

   /**
    * a method with the same name as the getter but "set" instead of "is" or "get", return type void and one
    * parameter being the type of the return of the getter
    *
    * @see #getGetter()
    */
   Optional<MethodLangModel> getSetter();

   /**
    * @see #getSetter()
    */
   MethodLangModel getSetterOrThrow();

   /**
    * has a {@link #getSetter()}
    */
   boolean isMutable();
}
