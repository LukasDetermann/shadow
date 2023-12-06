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

import static io.determann.shadow.api.modifier.Modifier.*;
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

   static IntersectionType getType(IntersectionType intersectionType)
   {
      return ((IntersectionImpl) intersectionType).getMirror();
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
      if (annotationable instanceof Declared)
      {
         Declared declared = ((Declared) annotationable);
         return getElement(declared);
      }
      if (annotationable instanceof Executable)
      {
         Executable executable = ((Executable) annotationable);
         return getElement(executable);
      }
      if (annotationable instanceof Generic)
      {
         Generic generic = ((Generic) annotationable);
         return getElement(generic);
      }
      if (annotationable instanceof Module)
      {
         Module module = ((Module) annotationable);
         return getElement(module);
      }
      if (annotationable instanceof Package)
      {
         Package aPackage = ((Package) annotationable);
         return getElement(aPackage);
      }
      if (annotationable instanceof Variable<?>)
      {
         Variable<?> variable = ((Variable<?>) annotationable);
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
      switch (modifier)
      {
         case PUBLIC:
            return PUBLIC;
         case PROTECTED:
            return PROTECTED;
         case PRIVATE:
            return PRIVATE;
         case ABSTRACT:
            return ABSTRACT;
         case STATIC:
            return STATIC;
         case FINAL:
            return FINAL;
         case STRICTFP:
            return STRICTFP;
         case DEFAULT:
            return DEFAULT;
         case TRANSIENT:
            return TRANSIENT;
         case VOLATILE:
            return VOLATILE;
         case SYNCHRONIZED:
            return SYNCHRONIZED;
         case NATIVE:
            return NATIVE;
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #getShadow(AnnotationProcessingContext, TypeMirror)
    */
   @SuppressWarnings("unchecked")
   static <SHADOW extends Shadow> SHADOW getShadow(AnnotationProcessingContext annotationProcessingContext, Element element)
   {
      switch (element.getKind())
      {
         case PACKAGE:
            return (SHADOW) new PackageImpl(annotationProcessingContext, (PackageElement) element);
         case ENUM:
         case ANNOTATION_TYPE:
            return (SHADOW) new DeclaredImpl(annotationProcessingContext, (TypeElement) element);
         case CLASS:
            return (SHADOW) new ClassImpl(annotationProcessingContext, (TypeElement) element);
         case INTERFACE:
            return (SHADOW) new InterfaceImpl(annotationProcessingContext, (TypeElement) element);
         case METHOD:
         case CONSTRUCTOR:
            return (SHADOW) new ExecutableImpl(annotationProcessingContext, ((ExecutableElement) element));
         case ENUM_CONSTANT:
            return (SHADOW) new EnumConstantImpl(annotationProcessingContext, (VariableElement) element);
         case FIELD:
            return (SHADOW) new FieldImpl(annotationProcessingContext, (VariableElement) element);
         case PARAMETER:
            return (SHADOW) new ParameterImpl(annotationProcessingContext, (VariableElement) element);
         case TYPE_PARAMETER:
            return (SHADOW) new GenericImpl(annotationProcessingContext, (TypeParameterElement) element);
         case MODULE:
            return (SHADOW) new ModuleImpl(annotationProcessingContext, (ModuleElement) element);
         case OTHER:
         case STATIC_INIT:
         case INSTANCE_INIT:
         case EXCEPTION_PARAMETER:
         case RESOURCE_VARIABLE:
         case LOCAL_VARIABLE:
            throw new IllegalArgumentException("not implemented");
         default:
            throw new IllegalArgumentException();
      }
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(AnnotationProcessingContext, Element)
    */
   @SuppressWarnings("unchecked")
   static <SHADOW extends Shadow> SHADOW getShadow(AnnotationProcessingContext annotationProcessingContext, TypeMirror typeMirror)
   {
      switch (typeMirror.getKind())
      {
         case BOOLEAN:
         case BYTE:
         case SHORT:
         case INT:
         case LONG:
         case CHAR:
         case FLOAT:
         case DOUBLE:
            return (SHADOW) new PrimitiveImpl(annotationProcessingContext, (PrimitiveType) typeMirror);
         case ARRAY:
            return (SHADOW) new ArrayImpl(annotationProcessingContext, (ArrayType) typeMirror);
         case DECLARED:
            switch (getProcessingEnv(annotationProcessingContext).getTypeUtils().asElement(typeMirror).getKind())
            {
               case CLASS:
                  return (SHADOW) new ClassImpl(annotationProcessingContext, ((DeclaredType) typeMirror));
               case INTERFACE:
                  return (SHADOW) new InterfaceImpl(annotationProcessingContext, (DeclaredType) typeMirror);
               case ANNOTATION_TYPE:
               case ENUM:
                  return (SHADOW) new DeclaredImpl(annotationProcessingContext, (DeclaredType) typeMirror);
               default:
                  throw new IllegalArgumentException("not implemented");
            }
         case WILDCARD:
            return (SHADOW) new WildcardImpl(annotationProcessingContext, (WildcardType) typeMirror);
         case VOID:
            return (SHADOW) new VoidImpl(annotationProcessingContext, ((NoType) typeMirror));
         case PACKAGE:
            return (SHADOW) new PackageImpl(annotationProcessingContext, (NoType) typeMirror);
         case MODULE:
            return (SHADOW) new ModuleImpl(annotationProcessingContext, (NoType) typeMirror);
         case NULL:
            return (SHADOW) new NullImpl(annotationProcessingContext, (NullType) typeMirror);
         case TYPEVAR:
            return (SHADOW) new GenericImpl(annotationProcessingContext, ((TypeVariable) typeMirror));
         case INTERSECTION:
            return (SHADOW) new IntersectionImpl(annotationProcessingContext, ((IntersectionType) typeMirror));
         case EXECUTABLE:
         case NONE:
            throw new IllegalArgumentException("bug in this api: executables should be created using elements");
         case ERROR:
         case OTHER:
         case UNION:
            throw new IllegalArgumentException("not implemented");
         default:
            throw new IllegalArgumentException();
      }
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
