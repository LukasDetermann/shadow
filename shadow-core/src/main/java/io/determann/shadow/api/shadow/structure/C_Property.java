package io.determann.shadow.api.shadow.structure;

import io.determann.shadow.api.shadow.C_Nameable;

/// This represents a java beans Property. Only a [Getter][io.determann.shadow.api.Operations#PROPERTY_GET_GETTER] is mandatory
///
/// [Getter][io.determann.shadow.api.Operations#PROPERTY_GET_GETTER]
/// - not static
/// - the name starts with "get" and is longer then 3 or
/// the name starts with "is" and has the return type [Boolean] or [Boolean#TYPE] and is longer then 2
///   - if a "is" and a "get" setter are present "is" is preferred
///
/// [Setter][io.determann.shadow.api.Operations#PROPERTY_GET_SETTER]
/// - not static
/// - the names has to match the getter name
/// - the name starts with "set" and is longer then 3
/// - has one parameter
/// - the parameter has the same type as the Getter
///
/// [Field][io.determann.shadow.api.Operations#PROPERTY_GET_FIELD]
/// - not static
/// - has the same type as the Getter
/// - if the name of the Getter without prefix is longer then 1 and has an uppercase Character
/// at index 0 or 1 the field name has to match exactly
/// or the name matches with the first Character converted to lowercase
///    - [Java Beans 8.8](https://www.oracle.com/java/technologies/javase/javabeans-spec.html)
public interface C_Property
      extends C_Nameable {}