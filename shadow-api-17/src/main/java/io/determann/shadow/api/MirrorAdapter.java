package io.determann.shadow.api;

import io.determann.shadow.api.annotation_processing.AnnotationProcessingContext;
import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.annotation_processing.AnnotationProcessingContextImpl;
import io.determann.shadow.impl.annotation_processing.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.impl.annotation_processing.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.impl.annotation_processing.shadow.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toUnmodifiableSet;

@JdkApi
public interface MirrorAdapter
{
   static AnnotationProcessingContext create(ProcessingEnvironment processingEnv, RoundEnvironment roundEnv, int processingRound)
   {
      return new AnnotationProcessingContextImpl(processingEnv, roundEnv, processingRound);
   }

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

   static Module getModule(AnnotationProcessingContext api, Element element)
   {
      return getShadow(api, getProcessingEnv(api).getElementUtils().getModuleOf(element));
   }

   static String getName(Element element)
   {
      return element.getSimpleName().toString();
   }

   static String getJavaDoc(AnnotationProcessingContext api, Element element)
   {
      return getProcessingEnv(api).getElementUtils().getDocComment(element);
   }

   static List<AnnotationUsage> getAnnotationUsages(AnnotationProcessingContext api, Element element)
   {
      return getAnnotationUsages(api, getProcessingEnv(api).getElementUtils().getAllAnnotationMirrors(element));
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(AnnotationProcessingContext api, Element element)
   {
      return getAnnotationUsages(api, element.getAnnotationMirrors());
   }

   static Set<Modifier> getModifiers(Element element)
   {
      return element.getModifiers().stream().map(MirrorAdapter::mapModifier).collect(toUnmodifiableSet());
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
    * @see #getShadow(AnnotationProcessingContext, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(AnnotationProcessingContext annotationProcessingContext, Element element)
   {
      return (SHADOW) switch (element.getKind())
      {
         case PACKAGE -> new PackageImpl(annotationProcessingContext, (PackageElement) element);
         case ENUM, ANNOTATION_TYPE -> new DeclaredImpl(annotationProcessingContext, (TypeElement) element);
         case RECORD -> new RecordImpl(annotationProcessingContext, (TypeElement) element);
         case CLASS -> new ClassImpl(annotationProcessingContext, (TypeElement) element);
         case INTERFACE -> new InterfaceImpl(annotationProcessingContext, (TypeElement) element);
         case METHOD, CONSTRUCTOR -> new ExecutableImpl(annotationProcessingContext, ((ExecutableElement) element));
         case ENUM_CONSTANT -> new EnumConstantImpl(annotationProcessingContext, (VariableElement) element);
         case FIELD -> new FieldImpl(annotationProcessingContext, (VariableElement) element);
         case PARAMETER -> new ParameterImpl(annotationProcessingContext, (VariableElement) element);
         case TYPE_PARAMETER -> new GenericImpl(annotationProcessingContext, (TypeParameterElement) element);
         case MODULE -> new ModuleImpl(annotationProcessingContext, (ModuleElement) element);
         case RECORD_COMPONENT -> new RecordComponentImpl(annotationProcessingContext, (RecordComponentElement) element);
         case OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE, BINDING_VARIABLE, LOCAL_VARIABLE ->
               throw new IllegalArgumentException("not implemented");
      };
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(AnnotationProcessingContext, Element)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(AnnotationProcessingContext annotationProcessingContext, TypeMirror typeMirror)
   {
      //noinspection unchecked
      return (SHADOW) switch (typeMirror.getKind())
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> new PrimitiveImpl(annotationProcessingContext, (PrimitiveType) typeMirror);
         case ARRAY -> new ArrayImpl(annotationProcessingContext, (ArrayType) typeMirror);
         case DECLARED -> switch (getProcessingEnv(annotationProcessingContext).getTypeUtils().asElement(typeMirror).getKind())
         {
            case CLASS -> new ClassImpl(annotationProcessingContext, ((DeclaredType) typeMirror));
            case INTERFACE -> new InterfaceImpl(annotationProcessingContext, (DeclaredType) typeMirror);
            case RECORD -> new RecordImpl(annotationProcessingContext, (DeclaredType) typeMirror);
            case ANNOTATION_TYPE, ENUM -> new DeclaredImpl(annotationProcessingContext, (DeclaredType) typeMirror);
            default -> throw new IllegalArgumentException("not implemented");
         };
         case WILDCARD -> new WildcardImpl(annotationProcessingContext, (WildcardType) typeMirror);
         case VOID -> new VoidImpl(annotationProcessingContext, ((NoType) typeMirror));
         case PACKAGE -> new PackageImpl(annotationProcessingContext, (NoType) typeMirror);
         case MODULE -> new ModuleImpl(annotationProcessingContext, (NoType) typeMirror);
         case NULL -> new NullImpl(annotationProcessingContext, (NullType) typeMirror);
         case TYPEVAR -> new GenericImpl(annotationProcessingContext, ((TypeVariable) typeMirror));
         case INTERSECTION -> new IntersectionImpl(annotationProcessingContext, ((IntersectionType) typeMirror));
         case EXECUTABLE, NONE -> throw new IllegalArgumentException("bug in this api: executables should be created using elements");
         case ERROR, OTHER, UNION -> throw new IllegalArgumentException("not implemented");
      };
   }

   static List<AnnotationUsage> getAnnotationUsages(AnnotationProcessingContext annotationProcessingContext, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(annotationProcessingContext, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue getAnnotationValue(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static ProcessingEnvironment getProcessingEnv(AnnotationProcessingContext annotationProcessingContext)
   {
      return ((AnnotationProcessingContextImpl) annotationProcessingContext).getProcessingEnv();
   }

   static RoundEnvironment getRoundEnv(AnnotationProcessingContext annotationProcessingContext)
   {
      return ((AnnotationProcessingContextImpl) annotationProcessingContext).getRoundEnv();
   }
}
