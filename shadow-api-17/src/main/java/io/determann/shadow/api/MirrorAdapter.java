package io.determann.shadow.api;

import io.determann.shadow.api.metadata.JdkApi;
import io.determann.shadow.api.modifier.Modifier;
import io.determann.shadow.api.shadow.Module;
import io.determann.shadow.api.shadow.Package;
import io.determann.shadow.api.shadow.Void;
import io.determann.shadow.api.shadow.*;
import io.determann.shadow.impl.ShadowApiImpl;
import io.determann.shadow.impl.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.impl.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.impl.shadow.*;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

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

   static RecordComponentElement getElement(RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }

   static VariableElement getElement(Variable<?> variable)
   {
      return ((VariableImpl) variable).getElement();
   }

   static Module getModule(ShadowApi api, Element element)
   {
      return getShadow(api, getProcessingEnv(api).getElementUtils().getModuleOf(element));
   }

   static String getSimpleName(Element element)
   {
      return element.getSimpleName().toString();
   }

   static String getJavaDoc(ShadowApi api, Element element)
   {
      return getProcessingEnv(api).getElementUtils().getDocComment(element);
   }

   static void logError(ShadowApi api, Element element, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
   }

   static void logInfo(ShadowApi api, Element element, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.NOTE, msg, element);
   }

   static void logWarning(ShadowApi api, Element element, String msg)
   {
      getProcessingEnv(api).getMessager().printMessage(Diagnostic.Kind.MANDATORY_WARNING, msg, element);
   }

   static List<AnnotationUsage> getAnnotationUsages(ShadowApi api, Element element)
   {
      return getAnnotationUsages(api, getProcessingEnv(api).getElementUtils().getAllAnnotationMirrors(element));
   }

   static List<AnnotationUsage> getDirectAnnotationUsages(ShadowApi api, Element element)
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
    * @see #getShadow(ShadowApi, TypeMirror)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(ShadowApi shadowApi, Element element)
   {
      return (SHADOW) switch (element.getKind())
      {
         case PACKAGE -> new PackageImpl(shadowApi, (PackageElement) element);
         case ENUM, ANNOTATION_TYPE -> new DeclaredImpl(shadowApi, (TypeElement) element);
         case RECORD -> new RecordImpl(shadowApi, (TypeElement) element);
         case CLASS -> new ClassImpl(shadowApi, (TypeElement) element);
         case INTERFACE -> new InterfaceImpl(shadowApi, (TypeElement) element);
         case METHOD, CONSTRUCTOR -> new ExecutableImpl(shadowApi, ((ExecutableElement) element));
         case ENUM_CONSTANT -> new EnumConstantImpl(shadowApi, (VariableElement) element);
         case FIELD -> new FieldImpl(shadowApi, (VariableElement) element);
         case PARAMETER -> new ParameterImpl(shadowApi, (VariableElement) element);
         case TYPE_PARAMETER -> new GenericImpl(shadowApi, (TypeParameterElement) element);
         case MODULE -> new ModuleImpl(shadowApi, (ModuleElement) element);
         case RECORD_COMPONENT -> new RecordComponentImpl(shadowApi, (RecordComponentElement) element);
         case OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE, BINDING_VARIABLE, LOCAL_VARIABLE ->
               throw new IllegalArgumentException("not implemented");
      };
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #getShadow(ShadowApi, Element)
    */
   static <SHADOW extends Shadow> SHADOW getShadow(ShadowApi shadowApi, TypeMirror typeMirror)
   {
      //noinspection unchecked
      return (SHADOW) switch (typeMirror.getKind())
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> new PrimitiveImpl(shadowApi, (PrimitiveType) typeMirror);
         case ARRAY -> new ArrayImpl(shadowApi, (ArrayType) typeMirror);
         case DECLARED -> switch (getProcessingEnv(shadowApi).getTypeUtils().asElement(typeMirror).getKind())
         {
            case CLASS -> new ClassImpl(shadowApi, ((DeclaredType) typeMirror));
            case INTERFACE -> new InterfaceImpl(shadowApi, (DeclaredType) typeMirror);
            case RECORD -> new RecordImpl(shadowApi, (DeclaredType) typeMirror);
            case ANNOTATION_TYPE, ENUM -> new DeclaredImpl(shadowApi, (DeclaredType) typeMirror);
            default -> throw new IllegalArgumentException("not implemented");
         };
         case WILDCARD -> new WildcardImpl(shadowApi, (WildcardType) typeMirror);
         case VOID -> new VoidImpl(shadowApi, ((NoType) typeMirror));
         case PACKAGE -> new PackageImpl(shadowApi, (NoType) typeMirror);
         case MODULE -> new ModuleImpl(shadowApi, (NoType) typeMirror);
         case NULL -> new NullImpl(shadowApi, (NullType) typeMirror);
         case TYPEVAR -> new GenericImpl(shadowApi, ((TypeVariable) typeMirror));
         case INTERSECTION -> new IntersectionImpl(shadowApi, ((IntersectionType) typeMirror));
         case EXECUTABLE, NONE -> throw new IllegalArgumentException("bug in this api: executables should be created using elements");
         case ERROR, OTHER, UNION -> throw new IllegalArgumentException("not implemented");
      };
   }

   static List<AnnotationUsage> getAnnotationUsages(ShadowApi shadowApi, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(shadowApi, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue getAnnotationValue(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static ProcessingEnvironment getProcessingEnv(ShadowApi shadowApi)
   {
      return ((ShadowApiImpl) shadowApi).getProcessingEnv();
   }

   static RoundEnvironment getRoundEnv(ShadowApi shadowApi)
   {
      return ((ShadowApiImpl) shadowApi).getRoundEnv();
   }
}
