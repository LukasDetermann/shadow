package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.AP;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.internal.annotation_processing.shadow.directive.*;
import io.determann.shadow.internal.annotation_processing.shadow.structure.*;
import io.determann.shadow.internal.annotation_processing.shadow.type.*;
import io.determann.shadow.internal.annotation_processing.shadow.type.primitive.PrimitiveImpl;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;

import static java.util.Collections.singletonList;

public interface Adapters
{
   //shadow -> jdk

   static AnnotationUsageAdapter adapt(AP.AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageAdapter(annotationUsage);
   }

   static DeclaredAdapter adapt(AP.Declared declared)
   {
      return new DeclaredAdapter(declared);
   }

   static ArrayAdapter adapt(AP.Array array)
   {
      return new ArrayAdapter(array);
   }

   static ExecutableAdapter adapt(AP.Executable executable)
   {
      return new ExecutableAdapter(executable);
   }

   static TypeAdapter adapt(AP.Type type)
   {
      return new TypeAdapter(type);
   }

   static GenericAdapter adapt(AP.Generic generic)
   {
      return new GenericAdapter(generic);
   }

   static ModuleAdapter adapt(AP.Module module)
   {
      return new ModuleAdapter(module);
   }

   static NullAdapter adapt(AP.Null aNull)
   {
      return new NullAdapter(aNull);
   }

   static PackageAdapter adapt(AP.Package aPackage)
   {
      return new PackageAdapter(aPackage);
   }

   static PrimitiveAdapter adapt(AP.Primitive primitive)
   {
      return new PrimitiveAdapter(primitive);
   }

   static VoidAdapter adapt(AP.Void aVoid)
   {
      return new VoidAdapter(aVoid);
   }

   static WildcardAdapter adapt(AP.Wildcard wildcard)
   {
      return new WildcardAdapter(wildcard);
   }

   static AnnotationableAdapter adapt(AP.Annotationable annotationable)
   {
      return new AnnotationableAdapter(annotationable);
   }

   static RecordComponentAdapter adapt(AP.RecordComponent recordComponent)
   {
      return new RecordComponentAdapter(recordComponent);
   }

   static VariableAdapter adapt(AP.Variable variable)
   {
      return new VariableAdapter(variable);
   }

   static ExportsAdapter adapt(AP.Exports exports)
   {
      return new ExportsAdapter(exports);
   }

   static OpensAdapter adapt(AP.Opens opens)
   {
      return new OpensAdapter(opens);
   }

   static RequiresAdapter adapt(AP.Requires requires)
   {
      return new RequiresAdapter(requires);
   }

   static UsesAdapter adapt(AP.Uses uses)
   {
      return new UsesAdapter(uses);
   }

   //jdk -> shadow

   static AP.Executable adapt(AP.Context context, ExecutableElement element)
   {
      return switch (element.getKind())
      {
         case METHOD -> new MethodImpl(context, element);
         case CONSTRUCTOR -> new ConstructorImpl(context, element);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.ExecutableElement that is nether a METHOD, FIELD or CONSTRUCTOR\n" + element);
      };
   }

   static AP.Variable adapt(AP.Context context, VariableElement element)
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

   static AP.RecordComponent adapt(AP.Context context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   static AP.Module adapt(AP.Context context, ModuleElement moduleElement)
   {
      return new ModuleImpl(context, moduleElement);
   }

   static AP.Wildcard adapt(AP.Context context, WildcardType wildcardType)
   {
      return new WildcardImpl(context, wildcardType);
   }

   static AP.Declared adapt(AP.Context context, TypeElement typeElement)
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

   static AP.Generic adapt(AP.Context context, TypeParameterElement typeParameterElement)
   {
      return new GenericImpl(context, typeParameterElement);
   }

   /// This Method constructs [Object]s based on [Element]s. They represent a specific usage. `List<String>` for example.
   /// If you want to represent the abstract code like `List<T>` then use [TypeMirror]s.
   static Object adapt(AP.Context context, Element element)
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

   /// This Method constructs [AP.Type]s based on [TypeMirror]s. They represent the abstract code. `List<T>` for example.
   /// If you want a specific usage like `List<String>` then you should use the specific [Element].
   ///
   /// [TypeKind#EXECUTABLE], [TypeKind#NONE], [TypeKind#PACKAGE], [TypeKind#MODULE] can not be used with this method as information would be
   /// missing, that is needed for there creation. Use [Element]s to create them.
   static AP.Type adapt(AP.Context context, TypeMirror typeMirror)
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
               " use LM_Adapters.adapt(LM_Context, javax.lang.model.element.Element) as they don't extend " + AP.Type.class.getSimpleName());
         case ERROR -> throw new IllegalArgumentException();
         case OTHER, UNION ->
               throw new IllegalArgumentException(typeMirror.getKind() + " is not exposed through annotation processing and not supported");
      };
   }

   static AP.Declared adapt(AP.Context context, DeclaredType declaredType)
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

   /// used to create [AP.Void].
   /// For [TypeKind#PACKAGE] use [#adapt(AP.Context , PackageElement)] and for [TypeKind#MODULE] use [#adapt(AP.Context , ModuleElement)].
   /// javax.lang.model has no specific type that represents [Void], this api can therefore not be more specific.
   static AP.Void adapt(AP.Context context, NoType noType)
   {
      if (!TypeKind.VOID.equals(noType.getKind()))
      {
         throw new IllegalArgumentException(
               "For javax.lang.model.type.NoType other then Void use LM_Adapters.adapt(LM_Context, Element/ModuleElement/PackageElement)");
      }
      return new VoidImpl(context, noType);
   }

   static AP.Generic adapt(AP.Context context, IntersectionType intersectionType)
   {
      return new IntersectionImpl(context, intersectionType);
   }

   static AP.Generic adapt(AP.Context context, TypeVariable typeVariable)
   {
      return new GenericImpl(context, typeVariable);
   }

   static AP.Null adapt(AP.Context context, NullType nullType)
   {
      return new NullImpl(context, nullType);
   }

   static AP.Array adapt(AP.Context context, ArrayType arrayType)
   {
      return new ArrayImpl(context, arrayType);
   }

   static AP.Primitive adapt(AP.Context context, PrimitiveType primitiveType)
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

   static AP.Package adapt(AP.Context context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static AP.AnnotationUsage adapt(AP.Context context, AnnotationMirror annotationMirror)
   {
      return adapt(context, singletonList(annotationMirror)).get(0);
   }

   static List<AP.AnnotationUsage> adapt(AP.Context context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static AP.Exports adapt(AP.Context context, ModuleElement.ExportsDirective exportsDirective)
   {
      return new ExportsImpl(context, exportsDirective);
   }

   static AP.Opens adapt(AP.Context context, ModuleElement.OpensDirective opensDirective)
   {
      return new OpensImpl(context, opensDirective);
   }

   static AP.Provides adapt(AP.Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      return new ProvidesImpl(context, providesDirective);
   }

   static AP.Requires adapt(AP.Context context, ModuleElement.RequiresDirective requiresDirective)
   {
      return new RequiresImpl(context, requiresDirective);
   }

   static AP.Uses adapt(AP.Context context, ModuleElement.UsesDirective usesDirective)
   {
      return new UsesImpl(context, usesDirective);
   }

   static AnnotationValue adapt(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static ContextAdapter adapt(AP.Context context)
   {
      return new ContextAdapter(context);
   }
}
