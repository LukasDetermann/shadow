package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.impl.shadow.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

import static io.determann.shadow.api.modifier.Modifier.*;
import static java.util.stream.Collectors.toUnmodifiableSet;

@JdkApi
public interface MirrorAdapter
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

   static Module getModule(ShadowApi api, Element element)
   {
      return getShadow(api, api.getJdkApiContext().getProcessingEnv().getElementUtils().getModuleOf(element));
   }

   static String getSimpleName(Element element)
   {
      return element.getSimpleName().toString();
   }

   static String getJavaDoc(ShadowApi api, Element element)
   {
      return api.getJdkApiContext().getProcessingEnv().getElementUtils().getDocComment(element);
   }

   static void logError(ShadowApi api, Element element, String msg)
   {
      api.getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
   }

   static void logInfo(ShadowApi api, Element element, String msg)
   {
      api.getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.NOTE, msg, element);
   }

   static void logWarning(ShadowApi api, Element element, String msg)
   {
      api.getJdkApiContext().getProcessingEnv().getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, element);
   }

   static List<AnnotationUsage> getAnnotationUsages(ShadowApi api, Element element)
   {
      return getAnnotationUsages(api, api.getJdkApiContext().getProcessingEnv().getElementUtils().getAllAnnotationMirrors(element));
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(ShadowApi api, Element element)
   {
      return getAnnotationUsages(api, element.getAnnotationMirrors());
   }

   public static Set<Modifier> getModifiers(Element element)
   {
      return element.getModifiers().stream().map(MirrorAdapter::mapModifier).collect(toUnmodifiableSet());
   }

   public static Modifier mapModifier(javax.lang.model.element.Modifier modifier)
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
    * @see #getShadow(ShadowApi, TypeMirror)
    */
   @SuppressWarnings("unchecked")
   static <SHADOW extends Shadow> SHADOW getShadow(ShadowApi shadowApi, @JdkApi Element element)
   {
      switch (element.getKind())
      {
         case PACKAGE:
            return (SHADOW) new PackageImpl(shadowApi, (PackageElement) element);
         case ENUM:
         case ANNOTATION_TYPE:
            return (SHADOW) new DeclaredImpl(shadowApi, (TypeElement) element);
         case CLASS:
            return (SHADOW) new ClassImpl(shadowApi, (TypeElement) element);
         case INTERFACE:
            return (SHADOW) new InterfaceImpl(shadowApi, (TypeElement) element);
         case METHOD:
         case CONSTRUCTOR:
            return (SHADOW) new ExecutableImpl(shadowApi, ((ExecutableElement) element));
         case ENUM_CONSTANT:
            return (SHADOW) new EnumConstantImpl(shadowApi, (VariableElement) element);
         case FIELD:
            return (SHADOW) new FieldImpl(shadowApi, (VariableElement) element);
         case PARAMETER:
            return (SHADOW) new ParameterImpl(shadowApi, (VariableElement) element);
         case TYPE_PARAMETER:
            return (SHADOW) new GenericImpl(shadowApi, (TypeParameterElement) element);
         case MODULE:
            return (SHADOW) new ModuleImpl(shadowApi, (ModuleElement) element);
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
    * @see #getShadow(ShadowApi, Element)
    */
   @SuppressWarnings("unchecked")
   static <SHADOW extends Shadow> SHADOW getShadow(ShadowApi shadowApi, @JdkApi TypeMirror typeMirror)
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
            return (SHADOW) new PrimitiveImpl(shadowApi, (PrimitiveType) typeMirror);
         case ARRAY:
            return (SHADOW) new ArrayImpl(shadowApi, (ArrayType) typeMirror);
         case DECLARED:
            switch (shadowApi.getJdkApiContext().getProcessingEnv().getTypeUtils().asElement(typeMirror).getKind())
            {
               case CLASS:
                  return (SHADOW) new ClassImpl(shadowApi, ((DeclaredType) typeMirror));
               case INTERFACE:
                  return (SHADOW) new InterfaceImpl(shadowApi, (DeclaredType) typeMirror);
               case ANNOTATION_TYPE:
               case ENUM:
                  return (SHADOW) new DeclaredImpl(shadowApi, (DeclaredType) typeMirror);
               default:
                  throw new IllegalArgumentException("not implemented");
            }
         case WILDCARD:
            return (SHADOW) new WildcardImpl(shadowApi, (WildcardType) typeMirror);
         case VOID:
            return (SHADOW) new VoidImpl(shadowApi, ((NoType) typeMirror));
         case PACKAGE:
            return (SHADOW) new PackageImpl(shadowApi, (NoType) typeMirror);
         case MODULE:
            return (SHADOW) new ModuleImpl(shadowApi, (NoType) typeMirror);
         case NULL:
            return (SHADOW) new NullImpl(shadowApi, (NullType) typeMirror);
         case TYPEVAR:
            return (SHADOW) new GenericImpl(shadowApi, ((TypeVariable) typeMirror));
         case INTERSECTION:
            return (SHADOW) new IntersectionImpl(shadowApi, ((IntersectionType) typeMirror));
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

   static List<AnnotationUsage> getAnnotationUsages(ShadowApi shadowApi, @JdkApi List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(shadowApi, annotationMirrors);
   }
}
