package io.determann.shadow.api.lang_model;

import io.determann.shadow.api.lang_model.shadow.AnnotationUsageLangModel;
import io.determann.shadow.api.lang_model.shadow.AnnotationableLangModel;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.shadow.modifier.Modifier;
import io.determann.shadow.api.shadow.type.Null;
import io.determann.shadow.api.shadow.type.Shadow;
import io.determann.shadow.api.shadow.type.Void;
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

public interface LangModelAdapter
{
   static AnnotationMirror particularize(AnnotationUsageLangModel annotationUsage)
   {
      return ((AnnotationUsageImpl) annotationUsage).getAnnotationMirror();
   }

   static DeclaredType particularType(DeclaredLangModel declared)
   {
      return ((DeclaredImpl) declared).getMirror();
   }

   static ArrayType particularType(ArrayLangModel array)
   {
      return ((ArrayImpl) array).getMirror();
   }

   static ExecutableType particularType(ExecutableLangModel executable)
   {
      return ((ExecutableImpl) executable).getMirror();
   }

   static TypeMirror particularType(ShadowLangModel shadow)
   {
      return ((ShadowImpl) shadow).getMirror();
   }

   static TypeVariable particularType(GenericLangModel generic)
   {
      return ((GenericImpl) generic).getMirror();
   }

   static IntersectionType particularType(IntersectionLangModel intersection)
   {
      return ((IntersectionImpl) intersection).getMirror();
   }

   static NoType particularType(ModuleLangModel module)
   {
      return ((ModuleImpl) module).getMirror();
   }

   static NullType particularType(Null aNull)
   {
      return ((NullImpl) aNull).getMirror();
   }

   static NoType particularType(PackageLangModel aPackage)
   {
      return ((PackageImpl) aPackage).getMirror();
   }

   static PrimitiveType particularType(PrimitiveLangModel primitive)
   {
      return ((PrimitiveImpl) primitive).getMirror();
   }

   static NoType particularType(Void aVoid)
   {
      return ((VoidImpl) aVoid).getMirror();
   }

   static WildcardType particularType(WildcardLangModel wildcard)
   {
      return ((WildcardImpl) wildcard).getMirror();
   }

   static Element particularElement(AnnotationableLangModel annotationable)
   {
      if (annotationable instanceof DeclaredLangModel declared)
      {
         return particularElement(declared);
      }
      if (annotationable instanceof ExecutableLangModel executable)
      {
         return particularElement(executable);
      }
      if (annotationable instanceof GenericLangModel generic)
      {
         return particularElement(generic);
      }
      if (annotationable instanceof ModuleLangModel module)
      {
         return particularElement(module);
      }
      if (annotationable instanceof PackageLangModel aPackage)
      {
         return particularElement(aPackage);
      }
      if (annotationable instanceof RecordComponentLangModel recordComponent)
      {
         return particularElement(recordComponent);
      }
      if (annotationable instanceof VariableLangModel variable)
      {
         return particularElement(variable);
      }
      throw new IllegalArgumentException();
   }

   static TypeElement particularElement(DeclaredLangModel declared)
   {
      return ((DeclaredImpl) declared).getElement();
   }

   static ExecutableElement particularElement(ExecutableLangModel executable)
   {
      return ((ExecutableImpl) executable).getElement();
   }

   static TypeParameterElement particularElement(GenericLangModel generic)
   {
      return ((GenericImpl) generic).getElement();
   }

   static ModuleElement particularElement(ModuleLangModel module)
   {
      return ((ModuleImpl) module).getElement();
   }

   static PackageElement particularElement(PackageLangModel aPackage)
   {
      return ((PackageImpl) aPackage).getElement();
   }

   static RecordComponentElement particularElement(RecordComponentLangModel recordComponent)
   {
      return ((RecordComponentImpl) recordComponent).getElement();
   }

   static VariableElement particularElement(VariableLangModel variable)
   {
      return ((VariableImpl) variable).getElement();
   }

   static Set<Modifier> getModifiers(Element element)
   {
      Set<Modifier> result = element.getModifiers().stream().map(LangModelAdapter::mapModifier).collect(Collectors.toSet());
      if ((element.getKind().isExecutable() || element.getKind().isDeclaredType() || element.getKind().isVariable()) &&
          !result.contains(Modifier.PUBLIC) &&
          !result.contains(Modifier.PROTECTED) &&
          !result.contains(Modifier.PRIVATE))
      {
         result.add(Modifier.PACKAGE_PRIVATE);
      }
      return Collections.unmodifiableSet(result);
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

   public static LangModelContext generalize(Types types, Elements elements)
   {
      return new LangModelContextImpl(types, elements);
   }

   static ExecutableLangModel generalize(LangModelContext context, ExecutableElement element)
   {
      return new ExecutableImpl(context, element);
   }

   public static RecordComponentLangModel generalize(LangModelContext context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   /**
    * {@link Element}s represent a usage. so for example a field may have the type {@code List<String>}. When you want the resulting {@link Shadow}
    * to represent {@code List<String>} and not just {@code List<T>} use the {@link Element} to create it.
    *
    * @see #generalize(LangModelContext, TypeMirror)
    */
   static <SHADOW extends ShadowLangModel> SHADOW generalize(LangModelContext context, Element element)
   {
      return (SHADOW) switch (element.getKind())
      {
         case PACKAGE -> new PackageImpl(context, (PackageElement) element);
         case ENUM -> new EnumImpl(context, (TypeElement) element);
         case ANNOTATION_TYPE -> new AnnotationImpl(context, (TypeElement) element);
         case RECORD -> new RecordImpl(context, (TypeElement) element);
         case CLASS -> new ClassImpl(context, (TypeElement) element);
         case INTERFACE -> new InterfaceImpl(context, (TypeElement) element);
         case ENUM_CONSTANT -> new EnumConstantImpl(context, (VariableElement) element);
         case FIELD -> new FieldImpl(context, (VariableElement) element);
         case PARAMETER -> new ParameterImpl(context, (VariableElement) element);
         case TYPE_PARAMETER -> new GenericImpl(context, (TypeParameterElement) element);
         case MODULE -> new ModuleImpl(context, (ModuleElement) element);
         case RECORD_COMPONENT -> new RecordComponentImpl(context, (RecordComponentElement) element);
         case OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE, BINDING_VARIABLE, LOCAL_VARIABLE, METHOD, CONSTRUCTOR ->
               throw new IllegalArgumentException("not implemented");
      };
   }

   /**
    * {@link TypeMirror}s represent the abstract code. {@code List<T>} for example.
    *
    * @see #generalize(LangModelContext, Element)
    */
   static <SHADOW extends ShadowLangModel> SHADOW generalize(LangModelContext context, TypeMirror typeMirror)
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

   static PackageLangModel generalizePackage(LangModelContext context, NoType aPackage)
   {
      return new PackageImpl(context, aPackage);
   }

   static PackageLangModel generalizePackage(LangModelContext context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static List<AnnotationUsageLangModel> generalize(LangModelContext context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static javax.lang.model.element.AnnotationValue generalize(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static Types getTypes(LangModelContext context)
   {
      return ((LangModelContextImplementation) context).getTypes();
   }

   static Elements getElements(LangModelContext context)
   {
      return ((LangModelContextImplementation) context).getElements();
   }
}
