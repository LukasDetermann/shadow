package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.modifier.C_Modifier;
import io.determann.shadow.api.shadow.type.C_Null;
import io.determann.shadow.api.shadow.type.C_Shadow;
import io.determann.shadow.api.shadow.type.C_Void;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.internal.lang_model.shadow.structure.*;
import io.determann.shadow.internal.lang_model.shadow.type.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface LM_Adapter
{
   static AnnotationMirror particularize(LM_AnnotationUsage annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }

   static DeclaredType particularType(LM_Declared declared)
   {
      return ((DeclaredImpl) declared).getMirror();
   }

   static ArrayType particularType(LM_Array array)
   {
      return ((ArrayImpl) array).getMirror();
   }

   static ExecutableType particularType(LM_Executable executable)
   {
      return ((ExecutableImpl) executable).getMirror();
   }

   static TypeMirror particularType(LM_Shadow shadow)
   {
      return ((ShadowImpl) shadow).getMirror();
   }

   static TypeVariable particularType(LM_Generic generic)
   {
      return ((GenericImpl) generic).getMirror();
   }

   static IntersectionType particularType(LM_Intersection intersection)
   {
      return ((IntersectionImpl) intersection).getMirror();
   }

   static NoType particularType(LM_Module module)
   {
      return ((ModuleImpl) module).getMirror();
   }

   static NullType particularType(C_Null aNull)
   {
      return ((NullImpl) aNull).getMirror();
   }

   static NoType particularType(LM_Package aPackage)
   {
      return ((PackageImpl) aPackage).getMirror();
   }

   static PrimitiveType particularType(LM_Primitive primitive)
   {
      return ((PrimitiveImpl) primitive).getMirror();
   }

   static NoType particularType(C_Void aVoid)
   {
      return ((VoidImpl) aVoid).getMirror();
   }

   static WildcardType particularType(LM_Wildcard wildcard)
   {
      return ((WildcardImpl) wildcard).getMirror();
   }

   static Element particularElement(LM_Annotationable annotationable)
   {
      if (annotationable instanceof LM_Declared declared)
      {
         return particularElement(declared);
      }
      if (annotationable instanceof LM_Executable executable)
      {
         return particularElement(executable);
      }
      if (annotationable instanceof LM_Generic generic)
      {
         return particularElement(generic);
      }
      if (annotationable instanceof LM_Module module)
      {
         return particularElement(module);
      }
      if (annotationable instanceof LM_Package aPackage)
      {
         return particularElement(aPackage);
      }
      if (annotationable instanceof LM_RecordComponent recordComponent)
      {
         return particularElement(recordComponent);
      }
      if (annotationable instanceof LM_Variable variable)
      {
         return particularElement(variable);
      }
      throw new IllegalArgumentException();
   }

   static TypeElement particularElement(LM_Declared declared)
   {
      return ((DeclaredImpl) declared).getElement();
   }

   static ExecutableElement particularElement(LM_Executable executable)
   {
      return ((ExecutableImpl) executable).getElement();
   }

   static TypeParameterElement particularElement(LM_Generic generic)
   {
      return ((GenericImpl) generic).getElement();
   }

   static ModuleElement particularElement(LM_Module module)
   {
      return ((ModuleImpl) module).getElement();
   }

   static PackageElement particularElement(LM_Package aPackage)
   {
      return ((PackageImpl) aPackage).getElement();
   }

   static RecordComponentElement particularElement(LM_RecordComponent recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }

   static VariableElement particularElement(LM_Variable variable)
   {
      return ((VariableImpl) variable).getElement();
   }

   static Set<C_Modifier> getModifiers(Element element)
   {
      Set<C_Modifier> result = element.getModifiers().stream().map(LM_Adapter::mapModifier).collect(Collectors.toSet());
      if ((element.getKind().isExecutable() || element.getKind().isDeclaredType() || element.getKind().isVariable()) &&
          !result.contains(C_Modifier.PUBLIC) &&
          !result.contains(C_Modifier.PROTECTED) &&
          !result.contains(C_Modifier.PRIVATE))
      {
         result.add(C_Modifier.PACKAGE_PRIVATE);
      }
      return Collections.unmodifiableSet(result);
   }

   static C_Modifier mapModifier(Modifier modifier)
   {
      return switch (modifier)
      {
         case PUBLIC -> C_Modifier.PUBLIC;
         case PROTECTED -> C_Modifier.PROTECTED;
         case PRIVATE -> C_Modifier.PRIVATE;
         case ABSTRACT -> C_Modifier.ABSTRACT;
         case STATIC -> C_Modifier.STATIC;
         case SEALED -> C_Modifier.SEALED;
         case NON_SEALED -> C_Modifier.NON_SEALED;
         case FINAL -> C_Modifier.FINAL;
         case STRICTFP -> C_Modifier.STRICTFP;
         case DEFAULT -> C_Modifier.DEFAULT;
         case TRANSIENT -> C_Modifier.TRANSIENT;
         case VOLATILE -> C_Modifier.VOLATILE;
         case SYNCHRONIZED -> C_Modifier.SYNCHRONIZED;
         case NATIVE -> C_Modifier.NATIVE;
      };
   }

   public static LM_Context generalize(Types types, Elements elements)
   {
      return new LangModelContextImpl(types, elements);
   }

   static LM_Executable generalize(LM_Context context, ExecutableElement element)
   {
      return new ExecutableImpl(context, element);
   }

   static LM_Variable generalize(LM_Context context, VariableElement element)
   {
      return switch (element.getKind())
      {
         case ENUM_CONSTANT -> new EnumConstantImpl(context, element);
         case FIELD -> new FieldImpl(context, element);
         case PARAMETER -> new ParameterImpl(context, element);
         default -> throw new IllegalArgumentException();
      };
   }

   public static LM_RecordComponent generalize(LM_Context context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link C_Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #generalize(LM_Context, TypeMirror)
    */
   static <SHADOW extends LM_Shadow> SHADOW generalize(LM_Context context, Element element)
   {
      return (SHADOW) switch (element.getKind())
      {
         case PACKAGE -> new PackageImpl(context, (PackageElement) element);
         case ENUM -> new EnumImpl(context, (TypeElement) element);
         case ANNOTATION_TYPE -> new AnnotationImpl(context, (TypeElement) element);
         case RECORD -> new RecordImpl(context, (TypeElement) element);
         case CLASS -> new ClassImpl(context, (TypeElement) element);
         case INTERFACE -> new InterfaceImpl(context, (TypeElement) element);
         case TYPE_PARAMETER -> new GenericImpl(context, (TypeParameterElement) element);
         case MODULE -> new ModuleImpl(context, (ModuleElement) element);
         case RECORD_COMPONENT -> new RecordComponentImpl(context, (RecordComponentElement) element);
         case METHOD, CONSTRUCTOR, PARAMETER, FIELD, ENUM_CONSTANT, OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE,
              BINDING_VARIABLE, LOCAL_VARIABLE -> throw new IllegalArgumentException("not implemented");
      };
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #generalize(LM_Context, Element)
    */
   static <SHADOW extends LM_Shadow> SHADOW generalize(LM_Context context, TypeMirror typeMirror)
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
            case ANNOTATION_TYPE -> new AnnotationImpl(context, (DeclaredType) typeMirror);
            case ENUM -> new EnumImpl(context, (DeclaredType) typeMirror);
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

   static LM_Package generalizePackage(LM_Context context, NoType aPackage)
   {
      return new PackageImpl(context, aPackage);
   }

   static LM_Package generalizePackage(LM_Context context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static List<LM_AnnotationUsage> generalize(LM_Context context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static AnnotationValue generalize(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static Types getTypes(LM_Context context)
   {
      return ((LM_ContextImplementation) context).getTypes();
   }

   static Elements getElements(LM_Context context)
   {
      return ((LM_ContextImplementation) context).getElements();
   }
}
