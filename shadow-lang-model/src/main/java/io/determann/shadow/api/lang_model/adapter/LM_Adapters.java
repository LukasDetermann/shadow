package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM_Context;
import io.determann.shadow.api.lang_model.shadow.LM_AnnotationUsage;
import io.determann.shadow.api.lang_model.shadow.LM_Annotationable;
import io.determann.shadow.api.lang_model.shadow.directive.*;
import io.determann.shadow.api.lang_model.shadow.structure.*;
import io.determann.shadow.api.lang_model.shadow.type.*;
import io.determann.shadow.api.lang_model.shadow.type.primitive.LM_Primitive;
import io.determann.shadow.internal.lang_model.LangModelContextImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.internal.lang_model.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.internal.lang_model.shadow.directive.*;
import io.determann.shadow.internal.lang_model.shadow.structure.*;
import io.determann.shadow.internal.lang_model.shadow.type.*;
import io.determann.shadow.internal.lang_model.shadow.type.primitive.PrimitiveImpl;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;

import static java.util.Collections.singletonList;

public interface LM_Adapters
{
   //shadow -> jdk

   static LM_AnnotationUsageAdapter adapt(LM_AnnotationUsage annotationUsage)
   {
      return new LM_AnnotationUsageAdapter(annotationUsage);
   }

   static LM_DeclaredAdapter adapt(LM_Declared declared)
   {
      return new LM_DeclaredAdapter(declared);
   }

   static LM_ArrayAdapter adapt(LM_Array array)
   {
      return new LM_ArrayAdapter(array);
   }

   static LM_ExecutableAdapter adapt(LM_Executable executable)
   {
      return new LM_ExecutableAdapter(executable);
   }

   static LM_TypeAdapter adapt(LM_Type type)
   {
      return new LM_TypeAdapter(type);
   }

   static LM_GenericAdapter adapt(LM_Generic generic)
   {
      return new LM_GenericAdapter(generic);
   }

   static LM_IntersectionAdapter adapt(LM_Intersection intersection)
   {
      return new LM_IntersectionAdapter(intersection);
   }

   static LM_ModuleAdapter adapt(LM_Module module)
   {
      return new LM_ModuleAdapter(module);
   }

   static LM_NullAdapter adapt(LM_Null aNull)
   {
      return new LM_NullAdapter(aNull);
   }

   static LM_PackageAdapter adapt(LM_Package aPackage)
   {
      return new LM_PackageAdapter(aPackage);
   }

   static LM_PrimitiveAdapter adapt(LM_Primitive primitive)
   {
      return new LM_PrimitiveAdapter(primitive);
   }

   static LM_VoidAdapter adapt(LM_Void aVoid)
   {
      return new LM_VoidAdapter(aVoid);
   }

   static LM_WildcardAdapter adapt(LM_Wildcard wildcard)
   {
      return new LM_WildcardAdapter(wildcard);
   }

   static LM_AnnotationableAdapter adapt(LM_Annotationable annotationable)
   {
      return new LM_AnnotationableAdapter(annotationable);
   }

   static LM_RecordComponentAdapter adapt(LM_RecordComponent recordComponent)
   {
      return new LM_RecordComponentAdapter(recordComponent);
   }

   static LM_VariableAdapter adapt(LM_Variable variable)
   {
      return new LM_VariableAdapter(variable);
   }

   static LM_ExportsAdapter adapt(LM_Exports exports)
   {
      return new LM_ExportsAdapter(exports);
   }

   static LM_OpensAdapter adapt(LM_Opens opens)
   {
      return new LM_OpensAdapter(opens);
   }

   static LM_RequiresAdapter adapt(LM_Requires requires)
   {
      return new LM_RequiresAdapter(requires);
   }

   static LM_UsesAdapter adapt(LM_Uses uses)
   {
      return new LM_UsesAdapter(uses);
   }

   //jdk -> shadow

   static LM_Context adapt(Types types, Elements elements)
   {
      return new LangModelContextImpl(types, elements);
   }

   static LM_Executable adapt(LM_Context context, ExecutableElement element)
   {
      return switch (element.getKind())
      {
         case METHOD -> new MethodImpl(context, element);
         case CONSTRUCTOR -> new ConstructorImpl(context, element);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.ExecutableElement that is nether a METHOD, FIELD or CONSTRUCTOR\n" + element);
      };
   }

   static LM_Variable adapt(LM_Context context, VariableElement element)
   {
      return switch (element.getKind())
      {
         case ENUM_CONSTANT -> new EnumConstantImpl(context, element);
         case FIELD -> new FieldImpl(context, element);
         case PARAMETER -> new ParameterImpl(context, element);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.VariableElement that is nether a ENUM_CONSTANT, FIELD or PARAMETER\n" + element);
      };
   }

   static LM_RecordComponent adapt(LM_Context context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   static LM_Module adapt(LM_Context context, ModuleElement moduleElement)
   {
      return new ModuleImpl(context, moduleElement);
   }

   static LM_Wildcard adapt(LM_Context context, WildcardType wildcardType)
   {
      return new WildcardImpl(context, wildcardType);
   }

   static LM_Declared adapt(LM_Context context, TypeElement typeElement)
   {
      return switch (typeElement.getKind())
      {
         case ENUM -> new EnumImpl(context, typeElement);
         case CLASS -> new ClassImpl(context, typeElement);
         case ANNOTATION_TYPE -> new AnnotationImpl(context, typeElement);
         case INTERFACE -> new InterfaceImpl(context, typeElement);
         case RECORD -> new RecordImpl(context, typeElement);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.TypeElement that is nether a ENUM, CLASS, ANNOTATION_TYPE, INTERFACE or RECORD\n" + typeElement);
      };
   }

   static LM_Generic adapt(LM_Context context, TypeParameterElement typeParameterElement)
   {
      return new GenericImpl(context, typeParameterElement);
   }

   /// This Method constructs [Object]s based on [Element]s. They represent a specific usage. `List<String>` for example.
   /// If you want to represent the abstract code like `List<T>` then use [TypeMirror]s.
   static Object adapt(LM_Context context, Element element)
   {
      return switch (element.getKind())
      {
         case PACKAGE -> adapt(context, (PackageElement) element);
         case ENUM, RECORD, ANNOTATION_TYPE, CLASS, INTERFACE -> adapt(context, (TypeElement) element);
         case TYPE_PARAMETER -> adapt(context, (TypeParameterElement) element);
         case MODULE -> adapt(context, (ModuleElement) element);
         case RECORD_COMPONENT -> adapt(context, (RecordComponentElement) element);
         case METHOD, CONSTRUCTOR -> adapt(context, ((ExecutableElement) element));
         case PARAMETER, FIELD, ENUM_CONSTANT -> adapt(context, ((VariableElement) element));
         case OTHER, STATIC_INIT, INSTANCE_INIT, EXCEPTION_PARAMETER, RESOURCE_VARIABLE,
              BINDING_VARIABLE, LOCAL_VARIABLE ->
               throw new IllegalArgumentException(element.getKind() + " is not exposed through annotation processing and not supported");
      };
   }

   /// This Method constructs [LM_Type]s based on [TypeMirror]s. They represent the abstract code. `List<T>` for example.
   /// If you want a specific usage like `List<String>` then you should use the specific [Element].
   ///
   /// [TypeKind#EXECUTABLE], [TypeKind#NONE], [TypeKind#PACKAGE], [TypeKind#MODULE] can not be used with this method as information would be
   /// missing, that is needed for there creation. Use [Element]s to create them.
   static LM_Type adapt(LM_Context context, TypeMirror typeMirror)
   {
      return switch (typeMirror.getKind())
      {
         case BOOLEAN, DOUBLE, BYTE, SHORT, INT, LONG, CHAR, FLOAT -> adapt(context, (PrimitiveType) typeMirror);
         case ARRAY -> adapt(context, (ArrayType) typeMirror);
         case DECLARED -> adapt(context, ((DeclaredType) typeMirror));
         case WILDCARD -> adapt(context, (WildcardType) typeMirror);
         case VOID -> adapt(context, ((NoType) typeMirror));
         case NULL -> adapt(context, (NullType) typeMirror);
         case TYPEVAR -> adapt(context, ((TypeVariable) typeMirror));
         case INTERSECTION -> adapt(context, ((IntersectionType) typeMirror));
         case EXECUTABLE, NONE, PACKAGE, MODULE -> throw new IllegalArgumentException(
               "for " +
               typeMirror.getKind() +
               " use LM_Adapters.adapt(LM_Context, javax.lang.model.element.Element) as they don't extend " + LM_Type.class.getSimpleName());
         case ERROR -> throw new IllegalArgumentException();
         case OTHER, UNION ->
               throw new IllegalArgumentException(typeMirror.getKind() + " is not exposed through annotation processing and not supported");
      };
   }

   static LM_Declared adapt(LM_Context context, DeclaredType declaredType)
   {
      return switch (adapt(context).toTypes().asElement(declaredType).getKind())
      {
         case ENUM -> new EnumImpl(context, declaredType);
         case CLASS -> new ClassImpl(context, declaredType);
         case ANNOTATION_TYPE -> new AnnotationImpl(context, declaredType);
         case INTERFACE -> new InterfaceImpl(context, declaredType);
         case RECORD -> new RecordImpl(context, declaredType);
         default -> throw new IllegalStateException(
               "javax.lang.model.type.DeclaredType that is nether a ENUM, CLASS, ANNOTATION_TYPE, INTERFACE or RECORD\n" + declaredType);
      };
   }

   /// used to create [LM_Void].
   /// For [TypeKind#PACKAGE] use [#adapt(LM_Context, PackageElement)] and for [TypeKind#MODULE] use [#adapt(LM_Context, ModuleElement)].
   /// javax.lang.model has no specific type that represents [Void], this api can therefore not be more specific.
   static LM_Void adapt(LM_Context context, NoType noType)
   {
      if (!TypeKind.VOID.equals(noType.getKind()))
      {
         throw new IllegalArgumentException(
               "For javax.lang.model.type.NoType other then Void use LM_Adapters.adapt(LM_Context, Element/ModuleElement/PackageElement)");
      }
      return new VoidImpl(context, noType);
   }

   static LM_Intersection adapt(LM_Context context, IntersectionType intersectionType)
   {
      return new IntersectionImpl(context, intersectionType);
   }

   static LM_Generic adapt(LM_Context context, TypeVariable typeVariable)
   {
      return new GenericImpl(context, typeVariable);
   }

   static LM_Null adapt(LM_Context context, NullType nullType)
   {
      return new NullImpl(context, nullType);
   }

   static LM_Array adapt(LM_Context context, ArrayType arrayType)
   {
      return new ArrayImpl(context, arrayType);
   }

   static LM_Primitive adapt(LM_Context context, PrimitiveType primitiveType)
   {
      return switch (primitiveType.getKind())
      {
         case BOOLEAN -> new PrimitiveImpl.LM_booleanImpl(context, primitiveType);
         case BYTE -> new PrimitiveImpl.LM_byteImpl(context, primitiveType);
         case SHORT -> new PrimitiveImpl.LM_shortImpl(context, primitiveType);
         case INT -> new PrimitiveImpl.LM_intImpl(context, primitiveType);
         case LONG -> new PrimitiveImpl.LM_longImpl(context, primitiveType);
         case CHAR -> new PrimitiveImpl.LM_charImpl(context, primitiveType);
         case FLOAT -> new PrimitiveImpl.LM_floatImpl(context, primitiveType);
         case DOUBLE -> new PrimitiveImpl.LM_doubleImpl(context, primitiveType);
         default -> throw new IllegalArgumentException(primitiveType.getClass().getName() +
                                                       " that is nether a BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT or DOUBLE\n" +
                                                       primitiveType);
      };
   }

   static LM_Package adapt(LM_Context context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static LM_AnnotationUsage adapt(LM_Context context, AnnotationMirror annotationMirror)
   {
      return adapt(context, singletonList(annotationMirror)).get(0);
   }

   static List<LM_AnnotationUsage> adapt(LM_Context context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static LM_Exports adapt(LM_Context context, ModuleElement.ExportsDirective exportsDirective)
   {
      return new ExportsImpl(context, exportsDirective);
   }

   static LM_Opens adapt(LM_Context context, ModuleElement.OpensDirective opensDirective)
   {
      return new OpensImpl(context, opensDirective);
   }

   static LM_Provides adapt(LM_Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      return new ProvidesImpl(context, providesDirective);
   }

   static LM_Requires adapt(LM_Context context, ModuleElement.RequiresDirective requiresDirective)
   {
      return new RequiresImpl(context, requiresDirective);
   }

   static LM_Uses adapt(LM_Context context, ModuleElement.UsesDirective usesDirective)
   {
      return new UsesImpl(context, usesDirective);
   }

   static AnnotationValue adapt(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static LM_ContextAdapter adapt(LM_Context context)
   {
      return new LM_ContextAdapter(context);
   }
}
