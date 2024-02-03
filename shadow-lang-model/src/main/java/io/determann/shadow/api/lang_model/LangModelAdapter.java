package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.Annotationable;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
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
      return element.getModifiers().stream().map(LangModelAdapter::mapModifier).collect(toUnmodifiableSet());
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
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> new PrimitiveImpl(context, (PrimitiveType) typeMirror);
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
         case EXECUTABLE, NONE -> throw new IllegalArgumentException("bug in this api: executables should be created using elements");
         case ERROR, OTHER, UNION -> throw new IllegalArgumentException("not implemented");
      };
   }

   static List<AnnotationUsage> getAnnotationUsages(LangModelContext context, List<? extends AnnotationMirror> annotationMirrors)
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
