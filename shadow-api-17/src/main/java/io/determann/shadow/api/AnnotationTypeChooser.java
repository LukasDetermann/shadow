package io.determann.shadow.api;

import io.determann.shadow.api.shadow.Class;
import io.determann.shadow.api.shadow.Enum;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Record;
import io.determann.shadow.api.shadow.*;

import java.lang.annotation.ElementType;
import java.util.Set;

/**
 * Provides annotated {@link Shadow}s of a specific type. Ths class has one Method for each {@link ElementType}.
 * <p>
 * {@link ElementType#TYPE_USE} is not supported
 *
 * @see ElementType
 * @see Shadow
 */
public interface AnnotationTypeChooser
{
   Set<Shadow> all();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   Set<Declared> declaredTypes();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   Set<Class> classes();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   Set<Enum> enums();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   Set<Interface> interfaces();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   Set<Record> records();

   /**
    * Annotations for {@link ElementType#FIELD}
    */
   Set<Field> fields();

   /**
    * Annotations for {@link ElementType#PARAMETER}
    */
   Set<Parameter> parameters();

   /**
    * Annotations for {@link ElementType#METHOD}
    */
   Set<Method> methods();

   /**
    * Annotations for {@link ElementType#CONSTRUCTOR}
    */
   Set<Constructor> constructors();

   /**
    * Annotations for {@link ElementType#ANNOTATION_TYPE}
    */
   Set<Annotation> annotations();

   /**
    * Annotations for {@link ElementType#PACKAGE}
    */
   Set<Package> packages();

   /**
    * Annotations for {@link ElementType#TYPE_PARAMETER}
    */
   Set<Generic> generics();

   /**
    * Annotations for {@link ElementType#MODULE}
    */
   Set<Module> modules();

   /**
    * Annotations for {@link ElementType#RECORD_COMPONENT}
    */
   Set<RecordComponent> recordComponents();
}
