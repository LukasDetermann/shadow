package io.determann.shadow.api.reflection;

import io.determann.shadow.api.C;
import io.determann.shadow.api.Modifier;
import io.determann.shadow.api.NestingKind;

import java.lang.annotation.ElementType;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static io.determann.shadow.api.Modifier.*;
import static io.determann.shadow.api.query.Operations.*;
import static io.determann.shadow.api.query.Provider.requestOrThrow;

public interface R
{
   /**
    * anything that can be annotated
    */
   interface Annotationable
         extends C.Annotationable
   {
      /**
       * returns all annotations. Annotations on parentClasses are included when they are annotated with {@link java.lang.annotation.Inherited}
       */
      List<AnnotationUsage> getAnnotationUsages();

      default List<AnnotationUsage> getUsagesOf(C.Annotation annotation)
      {
         return getAnnotationUsages().stream()
                                     .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                     .toList();
      }

      default Optional<AnnotationUsage> getUsageOf(C.Annotation annotation)
      {
         List<AnnotationUsage> usages = getUsagesOf(annotation);

         if (usages.isEmpty())
         {
            return Optional.empty();
         }
         if (usages.size() == 1)
         {
            return Optional.of(usages.get(0));
         }
         throw new IllegalArgumentException();
      }

      default AnnotationUsage getUsageOfOrThrow(C.Annotation annotation)
      {
         return getUsageOf(annotation).orElseThrow(IllegalArgumentException::new);
      }

      default boolean isAnnotatedWith(C.Annotation annotation)
      {
         return getAnnotationUsages().stream()
                                     .map(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION))
                                     .anyMatch(annotation1 -> annotation1.equals(annotation));
      }

      /**
       * returns all direkt annotations
       *
       * @see #getAnnotationUsages()
       */
      List<AnnotationUsage> getDirectAnnotationUsages();

      default List<AnnotationUsage> getDirectUsagesOf(C.Annotation annotation)
      {
         return getDirectAnnotationUsages().stream()
                                           .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                           .toList();
      }

      default Optional<AnnotationUsage> getDirectUsageOf(C.Annotation annotation)
      {
         return getDirectAnnotationUsages().stream()
                                           .filter(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION).equals(annotation))
                                           .findAny();
      }

      default AnnotationUsage getDirectUsageOfOrThrow(C.Annotation annotation)
      {
         return getDirectUsageOf(annotation).orElseThrow();
      }

      default boolean isDirectlyAnnotatedWith(C.Annotation annotation)
      {
         return getDirectAnnotationUsages().stream()
                                           .map(usage -> requestOrThrow(usage, ANNOTATION_USAGE_GET_ANNOTATION))
                                           .anyMatch(annotation1 -> annotation1.equals(annotation));
      }
   }

   /**
    * {@link C.Annotation} represents the java file for the java concept of an annotation. This on the other hand represents
    * a usage of such an annotation. like <br>
    * {@code @Documented("testValue) public class Test{ }}
    */
   interface AnnotationUsage
         extends C.AnnotationUsage
   {
      Map<Method, AnnotationValue> getValues();

      default AnnotationValue getValueOrThrow(String methodName)
      {
         return getValue(methodName).orElseThrow();
      }

      default Optional<AnnotationValue> getValue(String methodName)
      {
         return getValues().entrySet()
                           .stream()
                           .filter(entry -> requestOrThrow(entry.getKey(), NAMEABLE_GET_NAME).equals(methodName))
                           .map(Map.Entry::getValue)
                           .findAny();
      }

      Annotation getAnnotation();
   }

   sealed interface AnnotationValue
         extends C.AnnotationValue
   {
      /**
       * is this the default value specified in the annotation?
       */
      boolean isDefault();

      Object getValue();

      non-sealed interface StringValue
            extends AnnotationValue
      {
         @Override
         String getValue();
      }

      non-sealed interface BooleanValue
            extends AnnotationValue
      {
         @Override
         Boolean getValue();
      }

      non-sealed interface ByteValue
            extends AnnotationValue
      {
         @Override
         Byte getValue();
      }

      non-sealed interface ShortValue
            extends AnnotationValue
      {
         @Override
         Short getValue();
      }

      non-sealed interface IntegerValue
            extends AnnotationValue
      {
         @Override
         Integer getValue();
      }

      non-sealed interface LongValue
            extends AnnotationValue
      {
         @Override
         Long getValue();
      }

      non-sealed interface CharacterValue
            extends AnnotationValue
      {
         @Override
         Character getValue();
      }

      non-sealed interface FloatValue
            extends AnnotationValue
      {
         @Override
         Float getValue();
      }

      non-sealed interface DoubleValue
            extends AnnotationValue
      {
         @Override
         Double getValue();
      }

      non-sealed interface TypeValue
            extends AnnotationValue
      {
         @Override
         Type getValue();
      }

      non-sealed interface EnumConstantValue
            extends AnnotationValue
      {
         @Override
         EnumConstant getValue();
      }

      non-sealed interface AnnotationUsageValue
            extends AnnotationValue
      {
         @Override
         AnnotationUsage getValue();
      }

      non-sealed interface Values
            extends AnnotationValue
      {
         @Override
         List<AnnotationValue> getValue();
      }
   }

   interface Nameable
         extends C.Nameable
   {
      String getName();
   }

   interface QualifiedNameable
         extends C.QualifiedNameable
   {
      /**
       * a Qualified name is {@code javax.lang.model.element.QualifiedNameable}
       */
      String getQualifiedName();
   }

   non-sealed interface Annotation

         extends C.Annotation,
                 Declared,
                 StaticModifiable
   {
   }

   non-sealed interface Array

         extends C.Array,
                 ReferenceType
   {
      /**
       * returns true if this can be cast to that.
       * This can be useful if you want to check if a type implements for example a
       * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
       */
      boolean isSubtypeOf(C.Type type);

      /**
       * {@snippet :
       *  String[]//@highlight substring="String"
       *}
       */
      Type getComponentType();

      /**
       * returns Object[] for declared Arrays and an {@link C.Generic} with bounds of {@code java.io.Serializable&java.lang.Cloneable}
       * for primitive Arrays
       */
      List<Type> getDirectSuperTypes();

      /**
       * [] -> [][]
       */
      Array asArray();
   }

   non-sealed interface Class
         extends C.Class,
                 Declared,
                 AbstractModifiable,
                 StaticModifiable,
                 Sealable,
                 FinalModifiable
   {
      /**
       * reruns the super class of this class. calling {@code getSuperClass())} on {@link Integer} will return {@link Number}.
       * For {@link Object} null will be returned
       */
      Class getSuperClass();

      List<Class> getPermittedSubClasses();

      List<Property> getProperties();

      List<Constructor> getConstructors();

      /**
       * Equivalent to {@link #isSubtypeOf(C.Type)} except for primitives.
       * if one is a primitive and the other is not it tries to convert them
       */
      boolean isAssignableFrom(C.Type type);

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
       */
      List<Type> getGenericUsages();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
       */
      List<Generic> getGenericDeclarations();

      /**
       * Integer -&gt; int<br>
       * Long -&gt; long<br>
       * etc...
       */
      Primitive asUnboxed();
   }

   /**
    * Anything that can be a file.
    */
   sealed interface Declared
         extends C.Declared,
                 Annotationable,
                 StrictfpModifiable,
                 ReferenceType,
                 Nameable,
                 QualifiedNameable,
                 ModuleEnclosed,
                 Modifiable,
                 C.AccessModifiable
         permits Annotation,
                 Class,
                 Enum,
                 Interface,
                 Record
   {
      /**
       * returns true if this can be cast to that.
       * This can be useful if you want to check if a type implements for example a {@link java.util.Collection}
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
       */
      boolean isSubtypeOf(C.Type type);

      /**
       * is it an outer or inner class? etc.
       */
      NestingKind getNesting();

      default Field getFieldOrThrow(String simpleName)
      {
         return getFields().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
      }

      List<Field> getFields();

      default List<Method> getMethods(String simpleName)
      {
         return getMethods().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).toList();
      }

      List<Method> getMethods();

      /**
       * returns the parentClass including interfaces
       */
      List<Declared> getDirectSuperTypes();

      /**
       * returns all distinct supertypes including interfaces
       */
      Set<Declared> getSuperTypes();

      List<Interface> getInterfaces();

      default Interface getInterfaceOrThrow(String qualifiedName)
      {
         return getInterfaces().stream()
                               .filter(anInterface -> requestOrThrow(anInterface, QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                               .findAny()
                               .orElseThrow();
      }

      List<Interface> getDirectInterfaces();


      default Interface getDirectInterfaceOrThrow(String qualifiedName)
      {
         return getDirectInterfaces().stream()
                                     .filter(anInterface -> requestOrThrow(anInterface,
                                                                           QUALIFIED_NAMEABLE_GET_QUALIFIED_NAME).equals(qualifiedName))
                                     .findAny()
                                     .orElseThrow();
      }

      Package getPackage();

      /**
       * part of the java language specification:
       * <p>
       * The binary name of a top level type is its canonical name. (qualified name)
       * The binary name of a member type consists of the binary name of its immediately enclosing type, followed by $, followed by the simple name of the member.
       */
      String getBinaryName();

      Array asArray();

      /**
       * returns the {@link Declared} that surrounds this {@link Executable}
       */
      Optional<Declared> getSurrounding();

      default boolean isPublic()
      {
         return hasModifier(PUBLIC);
      }

      default boolean isPackagePrivate()
      {
         return hasModifier(PACKAGE_PRIVATE);
      }

      default boolean isProtected()
      {
         return hasModifier(PROTECTED);
      }

      default boolean isPrivate()
      {
         return hasModifier(PRIVATE);
      }
   }

   non-sealed interface Enum
         extends C.Enum,
                 Declared,
                 StaticModifiable
   {
      List<EnumConstant> getEumConstants();

      List<Constructor> getConstructors();

      default EnumConstant getEnumConstantOrThrow(String simpleName)
      {
         return getEumConstants().stream().filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName)).findAny().orElseThrow();
      }
   }

   /**
    * represents the generic parameter at a class, method, constructor etc.
    */
   non-sealed interface Generic
         extends C.Generic,
                 Annotationable,
                 ReferenceType,
                 Nameable
   {
      Type getBound();

      List<Type> getBounds();

      List<Interface> getAdditionalBounds();

      /**
       * returns the class, method constructor etc. this is the generic for
       */
      Object getEnclosing();
   }

   non-sealed interface Interface
         extends C.Interface,
                 Declared,
                 AbstractModifiable,
                 StaticModifiable,
                 Sealable
   {
      boolean isFunctional();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
       */
      List<Type> getGenericUsages();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
       */
      List<Generic> getGenericDeclarations();
   }

   non-sealed interface Null
         extends Type,
                 C.Null {}

   non-sealed interface Record
         extends C.Record,
                 Declared,
                 StaticModifiable,
                 FinalModifiable
   {
      default RecordComponent getRecordComponentOrThrow(String simpleName)
      {
         return getRecordComponents().stream()
                                     .filter(field -> requestOrThrow(field, NAMEABLE_GET_NAME).equals(simpleName))
                                     .findAny()
                                     .orElseThrow();
      }

      List<RecordComponent> getRecordComponents();

      List<Constructor> getConstructors();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenericTypes"}
       */
      List<Type> getGenericUsages();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
       */
      List<Generic> getGenericDeclarations();
   }

   sealed interface ReferenceType
         extends VariableType,
                 C.ReferenceType
         permits Array,
                 Declared,
                 Generic {}

   sealed interface Type
         extends C.Type
         permits Null,
                 VariableType,
                 Void,
                 Wildcard
   {
      /**
       * type equals from the compiler perspective. for example ? does not equal ? for the compiler
       */
      boolean representsSameType(C.Type type);
   }

   /// a type that can be used as method/ constructor parameter or field
   sealed interface VariableType
         extends C.VariableType,
                 Type
         permits ReferenceType,
                 Primitive {}

   non-sealed interface Void
         extends Type,
                 C.Void {}

   /**
    * {@snippet id = "test":
    *  List<? extends Number>//@highlight substring="? extends Number"
    *}
    * or
    * {@snippet :
    *  List<? super Number>//@highlight substring="? super Number"
    *}
    */
   non-sealed interface Wildcard

         extends C.Wildcard,
                 Type
   {
      /**
       * {@snippet :
       *  List<? extends Number>//@highlight substring="? extends Number"
       *}
       */
      Optional<Type> getExtends();

      /**
       * {@snippet :
       *  List<? super Number>//@highlight substring="? super Number"
       *}
       */
      Optional<Type> getSuper();

      /**
       * {@snippet file = "WildcardTest.java" region = "Wildcard.contains"}
       */
      boolean contains(C.Type type);
   }

   non-sealed interface boolean_
         extends Primitive,
                 C.boolean_ {}

   non-sealed interface byte_
         extends Primitive,
                 C.byte_ {}

   non-sealed interface char_
         extends Primitive,
                 C.char_ {}

   non-sealed interface double_
         extends Primitive,
                 C.double_ {}

   non-sealed interface float_
         extends Primitive,
                 C.float_ {}

   non-sealed interface int_
         extends Primitive,
                 C.int_ {}

   non-sealed interface long_
         extends Primitive,
                 C.long_ {}

   /**
    * represents primitive types, but not there wrapper classes. for example int, long, short
    */
   sealed interface Primitive
         extends C.Primitive,
                 VariableType,
                 Nameable
         permits boolean_,
                 byte_,
                 char_,
                 double_,
                 float_,
                 int_,
                 long_,
                 short_
   {
      /**
       * returns true if this can be cast to that.
       * This can be useful if you want to check if a type implements for example a
       * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
       */
      boolean isSubtypeOf(C.Type type);

      /**
       * Equivalent to {@link #isSubtypeOf(C.Type)} except for primitives.
       * if one is a primitive and the other is not it tries to convert them
       */
      boolean isAssignableFrom(C.Type type);

      /**
       * int -&gt; Integer<br>
       * long -&gt; Long<br>
       * etc...
       */
      Class asBoxed();

      /**
       * int -> int[]
       */
      Array asArray();
   }

   non-sealed interface short_
         extends Primitive,
                 C.short_ {}

   non-sealed interface Constructor
         extends C.Constructor,
                 Executable,
                 Modifiable,
                 C.AccessModifiable
   {
      default boolean isPublic()
      {
         return hasModifier(PUBLIC);
      }

      default boolean isPackagePrivate()
      {
         return hasModifier(PACKAGE_PRIVATE);
      }

      default boolean isProtected()
      {
         return hasModifier(PROTECTED);
      }

      default boolean isPrivate()
      {
         return hasModifier(PRIVATE);
      }
   }

   non-sealed interface EnumConstant
         extends C.EnumConstant,
                 Variable
   {
      @Override
      Enum getSurrounding();
   }

   sealed interface Executable
         extends C.Executable,
                 Annotationable,
                 Nameable,
                 Modifiable,
                 ModuleEnclosed
         permits Constructor,
                 Method
   {
      /**
       * {@snippet :
       *  public MyObject(String param){}//@highlight substring="String param"
       *}
       * Returns the formal parameters, meaning everything but the Receiver.
       */
      List<Parameter> getParameters();

      List<Type> getParameterTypes();

      List<Class> getThrows();

      /**
       * {@link List#of(Object[])}
       */
      boolean isVarArgs();

      /**
       * returns the {@link C.Declared} that surrounds this {@link Executable}
       */
      Declared getSurrounding();

      /**
       * {@snippet file = "GenericUsageTest.java" region = "GenericUsage.getGenerics"}
       */
      List<Generic> getGenericDeclarations();

      /**
       * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
       * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
       */
      Optional<Declared> getReceiverType();

      /**
       * The receiver represents the instance the method is called on. This language feature is barely used, it makes it possible to annotate "this".
       * {@snippet file = "ReceiverUsageTest.java" region = "ReceiverUsageTest.method"}
       */
      Optional<Receiver> getReceiver();
   }

   non-sealed interface Field
         extends C.Field,
                 Variable,
                 FinalModifiable,
                 StaticModifiable,
                 Modifiable,
                 C.AccessModifiable
   {
      boolean isConstant();

      /**
       * String or primitive value of static fields
       */
      Object getConstantValue();

      @Override
      Declared getSurrounding();

      default boolean isPublic()
      {
         return hasModifier(PUBLIC);
      }

      default boolean isPackagePrivate()
      {
         return hasModifier(PACKAGE_PRIVATE);
      }

      default boolean isProtected()
      {
         return hasModifier(PROTECTED);
      }

      default boolean isPrivate()
      {
         return hasModifier(PRIVATE);
      }
   }

   non-sealed interface Method
         extends C.Method,
                 Executable,
                 StaticModifiable,
                 DefaultModifiable,
                 AbstractModifiable,
                 FinalModifiable,
                 StrictfpModifiable,
                 NativeModifiable,
                 Modifiable,
                 C.AccessModifiable
   {
      Type getReturnType();

      boolean overrides(C.Method method);

      boolean overwrittenBy(C.Method method);

      /**
       * <pre>{@code
       * Do both methods have the same parameter types in the same order?
       * a() && b() -> true
       * a(String name) && b() -> false
       * a(String name, Long id) && b(Long id, String name) -> false
       * a(String name) && b(String name2) -> true
       * a(List list) && b(List<String> strings) -> true
       * a(List<String> strings) b(List list) -> false
       * }</pre>
       */
      boolean sameParameterTypes(C.Method method);

      /**
       * The java language and the java virtual machine have different specification. Bridge Methods are created to bridge that gap
       */
      boolean isBridge();

      /**
       * Can be annotated using annotations with {@link ElementType#TYPE_USE}
       */
      Result getResult();

      default boolean isPublic()
      {
         return hasModifier(PUBLIC);
      }

      default boolean isPackagePrivate()
      {
         return hasModifier(PACKAGE_PRIVATE);
      }

      default boolean isProtected()
      {
         return hasModifier(PROTECTED);
      }

      default boolean isPrivate()
      {
         return hasModifier(PRIVATE);
      }
   }

   interface Module
         extends C.Module,
                 Annotationable,
                 Nameable,
                 QualifiedNameable
   {
      List<Package> getPackages();

      /**
       * can everybody use reflection on this module?
       */
      boolean isOpen();

      boolean isUnnamed();

      boolean isAutomatic();

      /**
       * Relations between modules
       */
      List<Directive> getDirectives();

      List<Declared> getDeclared();

      Optional<Declared> getDeclared(String qualifiedName);

      default Declared getDeclaredOrThrow(String qualifiedName)
      {
         return getDeclared(qualifiedName).orElseThrow();
      }

      default List<Annotation> getAnnotations()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Annotation)
                             .map(declared -> ((Annotation) declared))
                             .toList();
      }

      default Optional<Annotation> getAnnotation(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Annotation.class::cast);
      }

      default Annotation getAnnotationOrThrow(String qualifiedName)
      {
         return ((Annotation) getDeclaredOrThrow(qualifiedName));
      }

      default List<Class> getClasses()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Class)
                             .map(declared -> ((Class) declared))
                             .toList();
      }

      default Optional<Class> getClass(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Class.class::cast);
      }

      default Class getClassOrThrow(String qualifiedName)
      {
         return ((Class) getDeclaredOrThrow(qualifiedName));
      }

      default List<Enum> getEnums()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Enum)
                             .map(declared -> ((Enum) declared))
                             .toList();
      }

      default Optional<Enum> getEnum(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Enum.class::cast);
      }

      default Enum getEnumOrThrow(String qualifiedName)
      {
         return ((Enum) getDeclaredOrThrow(qualifiedName));
      }

      default List<Interface> getInterfaces()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Interface)
                             .map(declared -> ((Interface) declared))
                             .toList();
      }

      default Optional<Interface> getInterface(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Interface.class::cast);
      }

      default Interface getInterfaceOrThrow(String qualifiedName)
      {
         return ((Interface) getDeclaredOrThrow(qualifiedName));
      }

      default List<Record> getRecords()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Record)
                             .map(declared -> ((Record) declared))
                             .toList();
      }

      default Optional<Record> getRecord(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Record.class::cast);
      }

      default Record getRecordOrThrow(String qualifiedName)
      {
         return ((Record) getDeclaredOrThrow(qualifiedName));
      }
   }

   interface ModuleEnclosed

         extends C.ModuleEnclosed
   {
      Module getModule();
   }

   interface Package
         extends C.Package,
                 Annotationable,
                 Nameable,
                 QualifiedNameable,
                 ModuleEnclosed
   {
      /**
       * Unnamed packages are intend for small snips of code like jShell and not seen in regular projects
       */
      boolean isUnnamed();

      List<Declared> getDeclared();

      Optional<Declared> getDeclared(String qualifiedName);

      default Declared getDeclaredOrThrow(String qualifiedName)
      {
         return getDeclared(qualifiedName).orElseThrow();
      }

      default List<Annotation> getAnnotations()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Annotation)
                             .map(declared -> ((Annotation) declared))
                             .toList();
      }

      default Optional<Annotation> getAnnotation(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Annotation.class::cast);
      }

      default Annotation getAnnotationOrThrow(String qualifiedName)
      {
         return ((Annotation) getDeclaredOrThrow(qualifiedName));
      }

      default List<Class> getClasses()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Class)
                             .map(declared -> ((Class) declared))
                             .toList();
      }

      default Optional<Class> getClass(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Class.class::cast);
      }

      default Class getClassOrThrow(String qualifiedName)
      {
         return ((Class) getDeclaredOrThrow(qualifiedName));
      }

      default List<Enum> getEnums()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Enum)
                             .map(declared -> ((Enum) declared))
                             .toList();
      }

      default Optional<Enum> getEnum(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Enum.class::cast);
      }

      default Enum getEnumOrThrow(String qualifiedName)
      {
         return ((Enum) getDeclaredOrThrow(qualifiedName));
      }

      default List<Interface> getInterfaces()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Interface)
                             .map(declared -> ((Interface) declared))
                             .toList();
      }

      default Optional<Interface> getInterface(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Interface.class::cast);
      }

      default Interface getInterfaceOrThrow(String qualifiedName)
      {
         return ((Interface) getDeclaredOrThrow(qualifiedName));
      }

      default List<Record> getRecords()
      {
         return getDeclared().stream()
                             .filter(declared -> declared instanceof Record)
                             .map(declared -> ((Record) declared))
                             .toList();
      }

      default Optional<Record> getRecord(String qualifiedName)
      {
         return getDeclared(qualifiedName).map(Record.class::cast);
      }

      default Record getRecordOrThrow(String qualifiedName)
      {
         return ((Record) getDeclaredOrThrow(qualifiedName));
      }
   }

   /// Parameter of a method or constructor
   ///
   /// @see Executable#getParameters()
   non-sealed interface Parameter
         extends C.Parameter,
                 Variable,
                 FinalModifiable
   {
      /**
       * {@link List#of(Object[])}
       */
      boolean isVarArgs();

      @Override
      Executable getSurrounding();
   }

   /// This represents a java beans Property. Only a [Getter][Property#getGetter()] is mandatory
   ///
   /// [Getter][Property#getGetter()]
   /// - not static
   /// - the name starts with "get" and is longer then 3 or
   /// the name starts with "is" and has the return type [Boolean] or [Boolean#TYPE] and is longer then 2
   ///   - if a "is" and a "get" setter are present "is" is preferred
   ///
   /// [Setter][Property#getSetter()]
   /// - not static
   /// - the names has to match the getter name
   /// - the name starts with "set" and is longer then 3
   /// - has one parameter
   /// - the parameter has the same type as the Getter
   ///
   /// [Field][Property#getField()]
   /// - not static
   /// - has the same type as the Getter
   /// - if the name of the Getter without prefix is longer then 1 and has an uppercase Character
   /// at index 0 or 1 the field name has to match exactly
   /// or the name matches with the first Character converted to lowercase
   ///    - [Java Beans 8.8](https://www.oracle.com/java/technologies/javase/javabeans-spec.html)
   interface Property

         extends C.Property,
                 Nameable
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
      VariableType getType();

      /**
       * a {@link C.Field} with the name and tye of this property
       *
       * @see #getName()
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
      Optional<Method> getSetter();

      /**
       * @see #getSetter()
       */
      Method getSetterOrThrow();

      /**
       * has a {@link #getSetter()}
       */
      boolean isMutable();
   }

   interface Receiver

         extends C.Receiver,
                 Annotationable
   {
      Type getType();
   }

   interface RecordComponent
         extends C.RecordComponent,
                 Annotationable,
                 Nameable,
                 ModuleEnclosed
   {
      /**
       * returns true if this can be cast to that.
       * This can be useful if you want to check if a type implements for example a
       * {@link java.util.Collection} {@snippet file = "GenericUsageTest.java" region = "GenericUsage.isSubtypeOf"}
       */
      boolean isSubtypeOf(C.Type type);

      /**
       * Equivalent to {@link #isSubtypeOf(C.Type)} except for primitives.
       * if one is a primitive and the other is not it tries to convert them
       */
      boolean isAssignableFrom(C.Type type);

      /**
       * returns the record this is a port of
       */
      Record getRecord();

      Type getType();

      Method getGetter();
   }

   interface Result
         extends C.Result,
                 Annotationable
   {
      Type getType();
   }

   sealed interface Variable
         extends C.Variable,
                 Annotationable,
                 Nameable,
                 ModuleEnclosed,
                 Modifiable
         permits EnumConstant,
                 Field,
                 Parameter
   {
      /**
       * returns true if this can be cast to that.
       * This can be useful if you want to check if a type implements for example a
       * {@link java.util.Collection} {@code typeToTest.erasure().isSubtypeOf(context.getDeclaredOrThrow("java.util.Collection").erasure())}
       */
      boolean isSubtypeOf(C.Type type);

      /**
       * Equivalent to {@link #isSubtypeOf(C.Type)} except for primitives.
       * if one is a primitive and the other is not it tries to convert them
       */
      boolean isAssignableFrom(C.Type type);

      VariableType getType();

      /**
       * The {@link Object} surrounding this {@link Variable}
       */
      Object getSurrounding();
   }

   interface AbstractModifiable
         extends Modifiable,
                 C.AbstractModifiable
   {
      default boolean isAbstract()
      {
         return hasModifier(ABSTRACT);
      }
   }

   interface DefaultModifiable
         extends Modifiable,
                 C.DefaultModifiable
   {
      default boolean isDefault()
      {
         return hasModifier(DEFAULT);
      }
   }

   interface FinalModifiable
         extends Modifiable,
                 C.FinalModifiable
   {
      default boolean isFinal()
      {
         return hasModifier(FINAL);
      }
   }

   interface Modifiable
         extends C.Modifiable
   {
      Set<Modifier> getModifiers();

      default boolean hasModifier(Modifier modifier)
      {
         return getModifiers().contains(modifier);
      }
   }

   interface NativeModifiable
         extends Modifiable,
                 C.NativeModifiable
   {
      default boolean isNative()
      {
         return hasModifier(NATIVE);
      }
   }

   interface Sealable
         extends Modifiable,
                 C.Sealable
   {
      default boolean isSealed()
      {
         return hasModifier(SEALED);
      }

      default boolean isNonSealed()
      {
         return hasModifier(NON_SEALED);
      }
   }

   interface StaticModifiable
         extends Modifiable,
                 C.StaticModifiable
   {
      default boolean isStatic()
      {
         return hasModifier(STATIC);
      }
   }

   interface StrictfpModifiable
         extends Modifiable,
                 C.StrictfpModifiable
   {
      default boolean isStrictfp()
      {
         return hasModifier(STRICTFP);
      }
   }

   /**
    * Relation between modules
    */
   sealed interface Directive
         extends C.Directive
         permits Exports,
                 Opens,
                 Provides,
                 Requires,
                 Uses {}

   /**
    * Exports a {@link #getPackage()} to {@link #getTargetModules()} or {@link #toAll()}
    */
   non-sealed interface Exports
         extends Directive,
                 C.Exports
   {
      /**
       * packages to export to every module in {@link #getTargetModules()} or all if the list is empty
       */
      Package getPackage();

      /**
       * The list of modules the package is exported to. or all if the list is empty
       */
      List<Module> getTargetModules();

      boolean toAll();
   }

   /**
    * Allows reflection access to {@link #getPackage()} for {@link #getTargetModules()} or {@link #toAll()}
    */
   non-sealed interface Opens
         extends Directive,
                 C.Opens
   {
      /**
       * the package to be accessed by reflection
       */
      Package getPackage();

      /**
       * Modules allowed to access {@link #getPackage()} or {@link #toAll()} if the list is empty
       */
      List<Module> getTargetModules();

      boolean toAll();
   }

   /**
    * Provides a {@link #getService()} to other modules internally using {@link #getImplementations()}
    *
    * @see Uses
    */
   non-sealed interface Provides
         extends Directive,
                 C.Provides
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

   /**
    * Dependency on another Module
    */
   non-sealed interface Requires
         extends Directive,
                 C.Requires
   {
      /**
       * The dependent module is required at compile time and optional at runtime
       */
      boolean isStatic();

      /**
       * Marks transitive Dependencies.
       * Let's say A depends on B
       * and B depends transitive on C.
       * In this case A also depends on C. This is needed when B exposes C
       */
      boolean isTransitive();

      Module getDependency();
   }

   /**
    * Uses a Service of another module
    *
    * @see Provides
    */
   non-sealed interface Uses
         extends Directive,
                 C.Uses
   {
      Declared getService();
   }
}
