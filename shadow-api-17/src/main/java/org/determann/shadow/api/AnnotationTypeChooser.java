package org.determann.shadow.api;

import org.determann.shadow.api.shadow.Class;
import org.determann.shadow.api.shadow.Enum;
import org.determann.shadow.api.shadow.Module;
import org.determann.shadow.api.shadow.Package;
import org.determann.shadow.api.shadow.Record;
import org.determann.shadow.api.shadow.*;
import org.jetbrains.annotations.UnmodifiableView;

import javax.lang.model.type.TypeMirror;
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
   @UnmodifiableView Set<Shadow<TypeMirror>> all();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   @UnmodifiableView Set<Declared> declaredTypes();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   @UnmodifiableView Set<Class> classes();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   @UnmodifiableView Set<Enum> enums();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   @UnmodifiableView Set<Interface> interfaces();

   /**
    * Annotations for {@link ElementType#TYPE}
    */
   @UnmodifiableView Set<Record> records();

   /**
    * Annotations for {@link ElementType#FIELD}
    */
   @UnmodifiableView Set<Field> fields();

   /**
    * Annotations for {@link ElementType#PARAMETER}
    */
   @UnmodifiableView Set<Parameter> parameters();

   /**
    * Annotations for {@link ElementType#METHOD}
    */
   @UnmodifiableView Set<Method> methods();

   /**
    * Annotations for {@link ElementType#CONSTRUCTOR}
    */
   @UnmodifiableView Set<Constructor> constructors();

   /**
    * Annotations for {@link ElementType#ANNOTATION_TYPE}
    */
   @UnmodifiableView Set<Annotation> annotations();

   /**
    * Annotations for {@link ElementType#PACKAGE}
    */
   @UnmodifiableView Set<Package> packages();

   /**
    * Annotations for {@link ElementType#TYPE_PARAMETER}
    */
   @UnmodifiableView Set<Generic> generics();

   /**
    * Annotations for {@link ElementType#MODULE}
    */
   @UnmodifiableView Set<Module> modules();

   /**
    * Annotations for {@link ElementType#RECORD_COMPONENT}
    */
   @UnmodifiableView Set<RecordComponent> recordComponents();
}
