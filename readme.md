# Shadow API

## Why Shadow API?

There are a lot of different Meta-Programming Apis. Each having unique
concepts and quirks. This makes Meta-Programming harder to understand
and creates a maintenance overhead. Once something is written with
java.lang.reflect it’s hard to change it to an Annotation Processor.
Some Apis like
[javax.lang.model](https://docs.oracle.com/en/java/javase/21/docs/api/java.compiler/javax/lang/model/package-summary.html),
are not up to the normal JDK standard.

Shadow API offers:

<details>
<summary>Straightforward data structure</summary>
<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 50%" />
<col style="width: 50%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="tableblock halign-left valign-top"><p>Shadow API</p></td>
<td class="tableblock halign-left valign-top"><p>JDK</p></td>
</tr>
<tr class="even">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="ulist">
<ul>
<li><p>Shadow</p>
<div class="ulist">
<ul>
<li><p>Declared</p>
<div class="ulist">
<ul>
<li><p>Class</p></li>
<li><p>Enum</p></li>
<li><p>Record</p></li>
<li><p>Annotation</p>
<div class="ulist">
<ul>
<li><p>AnnotationUsage</p></li>
</ul>
</div></li>
</ul>
</div></li>
<li><p>Array</p></li>
<li><p>Executable</p>
<div class="ulist">
<ul>
<li><p>Constructor</p></li>
<li><p>Method</p></li>
</ul>
</div></li>
<li><p>Intersection</p></li>
<li><p>Void</p></li>
<li><p>Module</p></li>
<li><p>Package</p></li>
<li><p>RecordComponent</p></li>
<li><p>Null</p></li>
<li><p>Primitive</p></li>
<li><p>Generic</p></li>
<li><p>Wildcard</p></li>
<li><p>Variable</p>
<div class="ulist">
<ul>
<li><p>EnumConstant</p></li>
<li><p>Field</p></li>
<li><p>Parameter</p></li>
</ul>
</div></li>
</ul>
</div></li>
</ul>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="paragraph">
<p>Cell with a list</p>
</div>
<div class="ulist">
<ul>
<li><p>TypeMirror</p>
<div class="ulist">
<ul>
<li><p>ReferenceType</p>
<div class="ulist">
<ul>
<li><p>ArrayType</p></li>
<li><p>DeclaredType</p></li>
<li><p>ErrorType</p></li>
<li><p>NullType</p></li>
<li><p>TypeVariable</p></li>
</ul>
</div></li>
<li><p>ExecutableType</p></li>
<li><p>IntersectionType</p></li>
<li><p>NoType</p></li>
<li><p>PrimitiveType</p></li>
<li><p>UnionType</p></li>
<li><p>WildcardType</p></li>
</ul>
</div></li>
<li><p>AnnotationMirror</p></li>
<li><p>Element</p>
<div class="ulist">
<ul>
<li><p>ExecutableElement</p></li>
<li><p>ModuleElement</p></li>
<li><p>PackageElement</p></li>
<li><p>RecordComponentElement</p></li>
<li><p>TypeElement</p></li>
<li><p>TypeParameterElement</p></li>
<li><p>VariableElement</p></li>
</ul>
</div></li>
</ul>
</div>
</div></td>
</tr>
</tbody>
</table>
</details>

<details>
<summary>Better documentation, especially for hard to understand topics</summary>

Shadow Api. The javadoc uses @snippet

```java
public interface LangModelContext
{
   //..

   /**
    * Used when constructing types to compare to at compile time that contain multiple,
    * on each other depended, generics.
    * it answers the question: given
    *
    * public class MyClass<A extends Comparable<B>, B extends Comparable<A>> {}
    *
    * and A being String what can B be by returning the "simplest" possible answer.
    * In this case String. The code for the example
    *
    * Class myClass = context.getClassOrThrow("MyClass");
    * Declared string = context.getDeclaredOrThrow("java.lang.String");
    * Class withGenerics = context.withGenerics(myClass,
    *                                         string,
    *                                         //the unboundWildcard will be replaced with the result
    *                                         context.getConstants().getUnboundWildcard());
    *
    * Class capture = context.interpolateGenerics(withGenerics);
    *
    * Shadow stringRep = Optional.of(capture.getGenericTypes().get(1))
    *                            .map(Converter::convert)
    *                            .map(ShadowConverter::toGenericOrThrow)
    *                            .map(Generic::getExtends)
    *                            .map(Converter::convert)
    *                            .map(ShadowConverter::toInterfaceOrThrow)
    *                            .map(Interface::getGenericTypes)
    *                            .map(shadows -> shadows.get(0))
    *                            .orElseThrow();
    *
    * Assertions.assertEquals(string, stringRep);
    */
   Class interpolateGenerics(Class aClass);

   //..
}
```

jdk

```java
public interface Types
{
   //...

   /**
    * {@return the erasure of a type}
    *
    * @param t  the type to be erased
    * @throws IllegalArgumentException if given a type for a package or module
    * @jls 4.6 Type Erasure
    */
   TypeMirror erasure(TypeMirror t);

   //...
}
```

</details>

<details>
<summary>Adapter for good JDK interoperability</summary>

javax.lang.model

```java
package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.shadow.Annotationable;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.structure.Module;
import io.determann.shadow.consistency.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.internal.lang_model.shadow.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

public interface LangModelAdapter
{
   static AnnotationMirror getMirror(AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }

   static DeclaredType getType(Declared declared)
   {
      return ((DeclaredImpl) declared).getMirror();
   }

   static ArrayType getType(Array array)
   {
      return ((ArrayImpl) array).getMirror();
   }

   static ExecutableType getType(Executable executable)
   {
      return ((ExecutableImpl) executable).getMirror();
   }

   static TypeMirror getType(Shadow shadow)
   {
      return ((ShadowImpl) shadow).getMirror();
   }

   static TypeVariable getType(Generic generic)
   {
      return ((GenericImpl) generic).getMirror();
   }

   static IntersectionType getType(Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getMirror();
   }

   static NoType getType(Module module)
   {
      return ((ModuleImpl) module).getMirror();
   }

   static NullType getType(Null aNull)
   {
      return ((NullImpl) aNull).getMirror();
   }

   static NoType getType(Package aPackage)
   {
      return ((PackageImpl) aPackage).getMirror();
   }

   static PrimitiveType getType(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getMirror();
   }

   static NoType getType(Void aVoid)
   {
      return ((VoidImpl) aVoid).getMirror();
   }

   static WildcardType getType(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getMirror();
   }

   static Element getElement(Annotationable annotationable)
   {
      if (annotationable instanceof Declared declared)
      {
         return getElement(declared);
      }
      if (annotationable instanceof Executable executable)
      {
         return getElement(executable);
      }
      if (annotationable instanceof Generic generic)
      {
         return getElement(generic);
      }
      if (annotationable instanceof Module module)
      {
         return getElement(module);
      }
      if (annotationable instanceof Package aPackage)
      {
         return getElement(aPackage);
      }
      if (annotationable instanceof RecordComponent recordComponent)
      {
         return getElement(recordComponent);
      }
      if (annotationable instanceof Variable<?> variable)
      {
         return getElement(variable);
      }
      throw new IllegalArgumentException();
   }

   static TypeElement getElement(Declared declared)
   {
      return ((DeclaredImpl) declared).getElement();
   }

   static ExecutableElement getElement(Executable executable)
   {
      return ((ExecutableImpl) executable).getElement();
   }

   static TypeParameterElement getElement(Generic generic)
   {
      return ((GenericImpl) generic).getElement();
   }

   static ModuleElement getElement(Module module)
   {
      return ((ModuleImpl) module).getElement();
   }

   static PackageElement getElement(Package aPackage)
   {
      return ((PackageImpl) aPackage).getElement();
   }

   static RecordComponentElement getElement(RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }

   static VariableElement getElement(Variable<?> variable)
   {
      return ((VariableImpl) variable).getElement();
   }

   static Module getModule(LangModelContext context, Element element)
   {
      return getShadow(context, getElements(context).getModuleOf(element));
   }

   static String getName(Element element)
   {
      return element.getSimpleName().toString();
   }

   static String getJavaDoc(LangModelContext context, Element element)
   {
      return getElements(context).getDocComment(element);
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context, Element element)
   {
      return getAnnotationUsages(context, getElements(context).getAllAnnotationMirrors(element));
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(LangModelContext context, Element element)
   {
      return getAnnotationUsages(context, element.getAnnotationMirrors());
   }

   static Set<Modifier> getModifiers(Element element)
   {
      return element.getModifiers()
                    .stream()
                    .map(LangModelAdapter::mapModifier)
                    .collect(toUnmodifiableSet());
   }

   static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> Modifier.PUBLIC;
         case PROTECTED -> Modifier.PROTECTED;
         case PRIVATE -> Modifier.PRIVATE;
         case ABSTRACT -> Modifier.ABSTRACT;
         case STATIC -> Modifier.STATIC;
         case SEALED -> Modifier.SEALED;
         case NON_SEALED -> Modifier.NON_SEALED;
         case FINAL -> Modifier.FINAL;
         case STRICTFP -> Modifier.STRICTFP;
         case DEFAULT -> Modifier.DEFAULT;
         case TRANSIENT -> Modifier.TRANSIENT;
         case VOLATILE -> Modifier.VOLATILE;
         case SYNCHRONIZED -> Modifier.SYNCHRONIZED;
         case NATIVE -> Modifier.NATIVE;
      };
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #getShadow(LangModelContext, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, Element element)
   {
      return (SHADOW) switch (element.getKind())
      {
         case PACKAGE -> new PackageImpl(context, (PackageElement) element);
         case ENUM, ANNOTATION_TYPE -> new DeclaredImpl(context, (TypeElement) element);
         case RECORD -> new RecordImpl(context, (TypeElement) element);
         case CLASS -> new ClassImpl(context, (TypeElement) element);
         case INTERFACE -> new InterfaceImpl(context, (TypeElement) element);
         case METHOD, CONSTRUCTOR -> new ExecutableImpl(context, ((ExecutableElement) element));
         case ENUM_CONSTANT -> new EnumConstantImpl(context, (VariableElement) element);
         case FIELD -> new FieldImpl(context, (VariableElement) element);
         case PARAMETER -> new ParameterImpl(context, (VariableElement) element);
         case TYPE_PARAMETER -> new GenericImpl(context, (TypeParameterElement) element);
         case MODULE -> new ModuleImpl(context, (ModuleElement) element);
         case RECORD_COMPONENT -> new RecordComponentImpl(context, (RecordComponentElement) element);
         case OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE, BINDING_VARIABLE, LOCAL_VARIABLE ->
               throw new IllegalArgumentException("not implemented");
      };
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(LangModelContext, Element)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, TypeMirror typeMirror)
   {
      //noinspection unchecked
      return (SHADOW) switch (typeMirror.getKind())
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE ->
               new PrimitiveImpl(context, (PrimitiveType) typeMirror);
         case ARRAY -> new ArrayImpl(context, (ArrayType) typeMirror);
         case DECLARED -> switch (getTypes(context).asElement(typeMirror).getKind())
         {
            case CLASS -> new ClassImpl(context, ((DeclaredType) typeMirror));
            case INTERFACE -> new InterfaceImpl(context, (DeclaredType) typeMirror);
            case RECORD -> new RecordImpl(context, (DeclaredType) typeMirror);
            case ANNOTATION_TYPE, ENUM -> new DeclaredImpl(context, (DeclaredType) typeMirror);
            default -> throw new IllegalArgumentException("not implemented");
         };
         case WILDCARD -> new WildcardImpl(context, (WildcardType) typeMirror);
         case VOID -> new VoidImpl(context, ((NoType) typeMirror));
         case PACKAGE -> new PackageImpl(context, (NoType) typeMirror);
         case MODULE -> new ModuleImpl(context, (NoType) typeMirror);
         case NULL -> new NullImpl(context, (NullType) typeMirror);
         case TYPEVAR -> new GenericImpl(context, ((TypeVariable) typeMirror));
         case INTERSECTION -> new IntersectionImpl(context, ((IntersectionType) typeMirror));
         case EXECUTABLE, NONE -> throw new IllegalArgumentException(
               "bug in this api: executables should be created using elements");
         case ERROR, OTHER, UNION -> throw new IllegalArgumentException("not implemented");
      };
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context,
                                                    List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue getAnnotationValue(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static Types getTypes(LangModelContext context)
   {
      return ((LangModelContextImpl) context).getTypes();
   }

   static Elements getElements(LangModelContext context)
   {
      return ((LangModelContextImpl) context).getElements();
   }
}
```

javax.annotation.processing

```java
package io.determann.shadow.api.annotation_processing;

import io.determann.shadow.consistency.shadow.Annotationable;
import io.determann.shadow.api.lang_model.LangModelAdapter;
import io.determann.shadow.api.lang_model.LangModelContext;
import io.determann.shadow.consistency.shadow.structure.Module;
import io.determann.shadow.consistency.shadow.structure.Package;
import io.determann.shadow.api.shadow.type.Void;
import io.determann.shadow.consistency.shadow.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;

public interface AnnotationProcessingAdapter
{
   static AnnotationMirror getMirror(AnnotationUsage annotationUsage)
   {
      return LangModelAdapter.getMirror(annotationUsage);
   }

   static DeclaredType getType(Declared declared)
   {
      return LangModelAdapter.getType(declared);
   }

   static ArrayType getType(Array array)
   {
      return LangModelAdapter.getType(array);
   }

   static ExecutableType getType(Executable executable)
   {
      return LangModelAdapter.getType(executable);
   }

   static TypeMirror getType(Shadow shadow)
   {
      return LangModelAdapter.getType(shadow);
   }

   static TypeVariable getType(Generic generic)
   {
      return LangModelAdapter.getType(generic);
   }

   static IntersectionType getType(Intersection intersection)
   {
      return LangModelAdapter.getType(intersection);
   }

   static NoType getType(io.determann.shadow.api.shadow.structure.Module module)
   {
      return LangModelAdapter.getType(module);
   }

   static NullType getType(Null aNull)
   {
      return LangModelAdapter.getType(aNull);
   }

   static NoType getType(io.determann.shadow.api.shadow.structure.Package aPackage)
   {
      return LangModelAdapter.getType(aPackage);
   }

   static PrimitiveType getType(Primitive primitive)
   {
      return LangModelAdapter.getType(primitive);
   }

   static NoType getType(Void aVoid)
   {
      return LangModelAdapter.getType(aVoid);
   }

   static WildcardType getType(Wildcard wildcard)
   {
      return LangModelAdapter.getType(wildcard);
   }

   static Element getElement(Annotationable annotationable)
   {
      return LangModelAdapter.getElement(annotationable);
   }

   static TypeElement getElement(Declared declared)
   {
      return LangModelAdapter.getElement(declared);
   }

   static ExecutableElement getElement(Executable executable)
   {
      return LangModelAdapter.getElement(executable);
   }

   static TypeParameterElement getElement(Generic generic)
   {
      return LangModelAdapter.getElement(generic);
   }

   static ModuleElement getElement(io.determann.shadow.api.shadow.structure.Module module)
   {
      return LangModelAdapter.getElement(module);
   }

   static PackageElement getElement(Package aPackage)
   {
      return LangModelAdapter.getElement(aPackage);
   }

   static RecordComponentElement getElement(RecordComponent recordComponent)
   {
      return LangModelAdapter.getElement(recordComponent);
   }

   static VariableElement getElement(Variable<?> variable)
   {
      return LangModelAdapter.getElement(variable);
   }

   static Module getModule(LangModelContext context, Element element)
   {
      return LangModelAdapter.getModule(context, element);
   }

   static String getName(Element element)
   {
      return LangModelAdapter.getName(element);
   }

   static String getJavaDoc(LangModelContext context, Element element)
   {
      return LangModelAdapter.getJavaDoc(context, element);
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context, Element element)
   {
      return LangModelAdapter.getAnnotationUsages(context, element);
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(LangModelContext context, Element element)
   {
      return LangModelAdapter.getDirectAnnotationUsages(context, element);
   }

   static Set<io.determann.shadow.api.modifier.Modifier> getModifiers(Element element)
   {
      return LangModelAdapter.getModifiers(element);
   }

   static io.determann.shadow.api.modifier.Modifier mapModifier(javax.lang.model.element.Modifier modifier)
   {
      return LangModelAdapter.mapModifier(modifier);
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #getShadow(LangModelContext, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, Element element)
   {
      return LangModelAdapter.getShadow(context, element);
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(LangModelContext, Element)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(LangModelContext context, TypeMirror typeMirror)
   {
      return LangModelAdapter.getShadow(context, typeMirror);
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context,
                                                    List<? extends AnnotationMirror> annotationMirrors)
   {
      return LangModelAdapter.getAnnotationUsages(context, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue getAnnotationValue(AnnotationValue annotationValue)
   {
      return LangModelAdapter.getAnnotationValue(annotationValue);
   }

   static Types getTypes(LangModelContext context)
   {
      return LangModelAdapter.getTypes(context);
   }

   static Elements getElements(LangModelContext context)
   {
      return LangModelAdapter.getElements(context);
   }
}
```

java.lang.reflect

```java
package io.determann.shadow.api.reflection;

import io.determann.shadow.api.shadow.type.Array;
import io.determann.shadow.api.shadow.structure.Constructor;
import io.determann.shadow.api.shadow.structure.Executable;
import io.determann.shadow.consistency.shadow.structure.Field;
import io.determann.shadow.consistency.shadow.structure.Method;
import io.determann.shadow.consistency.shadow.structure.Module;
import io.determann.shadow.api.shadow.structure.Package;
import io.determann.shadow.api.shadow.structure.Parameter;
import io.determann.shadow.api.shadow.structure.RecordComponent;
import io.determann.shadow.consistency.shadow.*;
import io.determann.shadow.consistency.shadow.module.*;
import io.determann.shadow.internal.reflection.NamedSupplier;
import io.determann.shadow.internal.reflection.shadow.*;
import io.determann.shadow.internal.reflection.shadow.directive.*;

import java.lang.Class;
import java.lang.Enum;
import java.lang.Void;
import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleFinder;
import java.lang.module.ModuleReference;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ReflectionAdapter
{
   public static <SHADOW extends Shadow> SHADOW getShadow(Class<?> aClass)
   {
      return getShadow(aClass, Collections.emptyList());
   }

   @SuppressWarnings("unchecked")
   private static <SHADOW extends Shadow> SHADOW getShadow(Class<?> aClass, List<Shadow> genericTypes)
   {
      if (aClass.isPrimitive())
      {
         if (aClass.equals(Void.TYPE))
         {
            return (SHADOW) new VoidImpl();
         }
         return (SHADOW) new PrimitiveImpl(aClass);
      }
      if (aClass.isArray())
      {
         return (SHADOW) new ArrayImpl(aClass);
      }
      if (aClass.isRecord())
      {
         return (SHADOW) new RecordImpl(aClass, genericTypes);
      }
      if (aClass.isAnnotation() || aClass.isEnum())
      {
         return (SHADOW) new DeclaredImpl(aClass);
      }
      if (aClass.isInterface())
      {
         return (SHADOW) new InterfaceImpl(aClass, genericTypes);
      }
      return (SHADOW) new ClassImpl(aClass, genericTypes);
   }

   public static Package getShadow(java.lang.Package aPackage)
   {
      return new PackageImpl(new NamedSupplier<>(aPackage, java.lang.Package::getName));
   }

   public static Module getShadow(java.lang.Module module)
   {
      return new ModuleImpl(new NamedSupplier<>(module.getDescriptor(), ModuleDescriptor::name),
                            Arrays.stream(module.getAnnotations())
                                  .map(ReflectionAdapter::getAnnotationUsage)
                                  .toList());
   }

   public static Module getModuleShadow(String name)
   {
      return new ModuleImpl(new NamedSupplier<>(name,
                                                () -> ModuleFinder.ofSystem()
                                                                  .find(name)
                                                                  .orElseThrow()
                                                                  .descriptor(),
                                                ModuleDescriptor::name));
   }

   public static Module getShadow(ModuleReference moduleReference)
   {
      return new ModuleImpl(new NamedSupplier<>(moduleReference.descriptor(),
                                                ModuleDescriptor::name));
   }

   public static Shadow getShadow(Type type)
   {
      if (type instanceof ParameterizedType parameterizedType)
      {
         Type rawType = parameterizedType.getRawType();
         if (rawType instanceof Class<?> aClass)
         {
            return getShadow(aClass,
                             Arrays.stream(parameterizedType.getActualTypeArguments())
                                   .map(ReflectionAdapter::getShadow)
                                   .toList());
         }
         throw new IllegalStateException();
      }
      if (type instanceof TypeVariable<?> typeVariable)
      {
         return new GenericImpl(typeVariable);
      }
      if (type instanceof Class<?> aClass)
      {
         return getShadow(aClass);
      }
      if (type instanceof WildcardType wildcardType)
      {
         return new WildcardImpl(wildcardType);
      }
      throw new IllegalStateException();
   }

   public static AnnotationUsage getAnnotationUsage(java.lang.annotation.Annotation annotation)
   {
      return new AnnotationUsageImpl(annotation);
   }

   public static Field getShadow(java.lang.reflect.Field field)
   {
      return new FieldImpl(field);
   }

   public static Method getShadow(java.lang.reflect.Method method)
   {
      return new ExecutableImpl(method);
   }

   public static Constructor getShadow(java.lang.reflect.Constructor<?> constructor)
   {
      return new ExecutableImpl(constructor);
   }

   public static RecordComponent getShadow(java.lang.reflect.RecordComponent recordComponent)
   {
      return new RecordComponentImpl(recordComponent);
   }

   public static Parameter getShadow(java.lang.reflect.Parameter parameter)
   {
      return new ParameterImpl(parameter);
   }

   public static Shadow getShadow(TypeVariable<?> typeVariable)
   {
      return new GenericImpl(typeVariable);
   }

   public static Requires getShadow(ModuleDescriptor.Requires requires)
   {
      return new RequiresImpl(requires);
   }

   public static Exports getShadow(ModuleDescriptor.Exports exports)
   {
      return new ExportsImpl(exports);
   }

   public static Opens getShadow(ModuleDescriptor.Opens opens)
   {
      return new OpensImpl(opens);
   }

   public static Provides getShadow(ModuleDescriptor.Provides provides)
   {
      return new ProvidesImpl(provides);
   }

   public static Uses getUsesShadow(String uses)
   {
      return new UsesImpl(uses);
   }

   public static Optional<Declared> getDeclaredShadow(String qualifiedName)
   {
      try
      {
         Class<?> aClass = Class.forName(qualifiedName);
         return Optional.of(getShadow(aClass));
      }
      catch (ClassNotFoundException e)
      {
         return Optional.empty();
      }
   }

   public static Executable getShadow(java.lang.reflect.Executable executable)
   {
      return new ExecutableImpl(executable);
   }

   public static Package getPackageShadow(String name)
   {
      return new PackageImpl(new NamedSupplier<>(name, () ->
      {
         java.lang.Package aPackage = java.lang.Package.getPackage(name);
         if (aPackage == null)
         {
            throw new IllegalArgumentException("Package \"" +
                                               name +
                                               "\" not found. The VM did not load it yet");
         }
         return aPackage;
      }, java.lang.Package::getName));
   }

   public static EnumConstant getShadow(Enum<?> enumConstant)
   {
      try
      {
         return new EnumConstantImpl(enumConstant.getDeclaringClass().getField(enumConstant.name()));
      }
      catch (NoSuchFieldException e)
      {
         throw new RuntimeException(e);
      }
   }

   public static Shadow getShadow(GenericDeclaration genericDeclaration)
   {
      if (genericDeclaration instanceof Class<?> aClass)
      {
         return getShadow(aClass);
      }
      if (genericDeclaration instanceof java.lang.reflect.Executable executable)
      {
         return getShadow(executable);
      }
      throw new IllegalStateException();
   }

   public static ModuleDescriptor.Exports getReflection(Exports exports)
   {
      return ((ExportsImpl) exports).getReflection();
   }

   public static ModuleDescriptor.Opens getReflection(Opens opens)
   {
      return ((OpensImpl) opens).getReflection();
   }

   public static ModuleDescriptor.Provides getReflection(Provides provides)
   {
      return ((ProvidesImpl) provides).getReflection();
   }

   public static ModuleDescriptor.Requires getReflection(Requires requires)
   {
      return ((RequiresImpl) requires).getReflection();
   }

   public static String getReflection(Uses uses)
   {
      return ((UsesImpl) uses).getReflection();
   }

   public static java.lang.annotation.Annotation getReflection(AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationReflection();
   }

   public static Class<?> getReflection(Array array)
   {
      return ((ArrayImpl) array).getReflection();
   }

   public static Class<?> getReflection(Declared declared)
   {
      return ((DeclaredImpl) declared).getReflection();
   }

   public static java.lang.reflect.Field getReflection(EnumConstant enumConstant)
   {
      return ((ReflectionFieldImpl<?>) enumConstant).getReflection();
   }

   public static java.lang.reflect.Executable getReflection(Executable executable)
   {
      return ((ExecutableImpl) executable).getReflection();
   }

   public static java.lang.reflect.Field getReflection(Field field)
   {
      return ((ReflectionFieldImpl<?>) field).getReflection();
   }

   public static TypeVariable<?> getReflection(Generic generic)
   {
      return ((GenericImpl) generic).getReflection();
   }

   public static Type[] getReflection(Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getReflection();
   }

   public static ModuleDescriptor getReflection(Module module)
   {
      return ((ModuleImpl) module).getReflection();
   }

   public static java.lang.Package getReflection(Package aPackage)
   {
      return ((PackageImpl) aPackage).getReflection();
   }

   public static java.lang.reflect.Parameter getReflection(Parameter parameter)
   {
      return ((ParameterImpl) parameter).getReflection();
   }

   public static java.lang.Class<?> getReflection(Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getReflection();
   }

   public static java.lang.reflect.RecordComponent getReflection(RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getReflection();
   }

   public static WildcardType getReflection(Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getReflection();
   }
}
```

</details>

<details>
<summary>Better rendering of existing sources</summary>

A simple method like this

```java
public abstract class MyClass
{

   @MyAnnotation
   public abstract <T> T get(int index);
}
```

can be rendered in the following ways

<table class="tableblock frame-all grid-all stretch">
<colgroup>
<col style="width: 33%" />
<col style="width: 33%" />
<col style="width: 33%" />
</colgroup>
<tbody>
<tr class="odd">
<td class="tableblock halign-left valign-top"><p>Rendering</p></td>
<td class="tableblock halign-left valign-top"><p>Shadow AOI</p></td>
<td class="tableblock halign-left valign-top"><p>JDK</p></td>
</tr>
<tr class="even">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>@MyAnnotation
public abstract &lt;T&gt; T get(int index);</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>render(method).declaration()</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
&#10;</div></td>
</tr>
<tr class="odd">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>@MyAnnotation
public &lt;T&gt; T get(int index) {
}</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>render(method).declaration(&quot;//do stuff&quot;)</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
&#10;</div></td>
</tr>
<tr class="even">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>get()</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>render(method).invocation()</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
&#10;</div></td>
</tr>
<tr class="odd">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>get(5413)</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>render(method).invocation(&quot;5413&quot;)</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
&#10;</div></td>
</tr>
<tr class="even">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>&lt;T&gt;get(int)</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>method.toString()</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>methodElement.toString()</code></pre>
</div>
</div>
</div></td>
</tr>
<tr class="odd">
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>&lt;T&gt;(int)T</code></pre>
</div>
</div>
</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
&#10;</div></td>
<td class="tableblock halign-left valign-top"><div class="content">
<div class="listingblock">
<div class="content">
<pre class="CodeRay highlight"><code>methodMirror.toString()</code></pre>
</div>
</div>
</div></td>
</tr>
</tbody>
</table>

Names can be rendered as

- QualifiedNames
- SimpleNames
- WithoutNeedingImports (default)

and a Callback can be registered for NameRenderedEvents to create for
example imports.

</details>

### Currently Supported are:

- [Annotation Processing](https://www.shadow.determann.io/Shadow-Api/Annotation%20Processing.html)
- [java.lang.model](https://www.shadow.determann.io/Shadow-Api/java.lang.model.html)
- [reflection](https://www.shadow.determann.io/Shadow-Api/Reflection.html)

### API Goals

#### Short-term

Make annotation processing more accessible and easier to comprehend.

#### Long-term

Provide an API for any kind of Meta-Programming. annotation processing,
reflection, doclet etc.

### Project status

The Annotation Processing API is relativity stable. The Reflection API
is missing some important parts, like invocations and needs more time.