package io.determann.shadow.api.annotation_processing.adapter;

import io.determann.shadow.api.annotation_processing.Ap;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.internal.annotation_processing.annotationvalue.AnnotationValueImpl;
import io.determann.shadow.internal.annotation_processing.shadow.directive.*;
import io.determann.shadow.internal.annotation_processing.shadow.structure.*;
import io.determann.shadow.internal.annotation_processing.shadow.type.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;

import static java.util.Collections.singletonList;

public interface Adapters
{
   //shadow -> jdk

   static AnnotationUsageAdapter adapt(Ap.AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageAdapter(annotationUsage);
   }

   static DeclaredAdapter adapt(Ap.Declared declared)
   {
      return new DeclaredAdapter(declared);
   }

   static ArrayAdapter adapt(Ap.Array array)
   {
      return new ArrayAdapter(array);
   }

   static ExecutableAdapter adapt(Ap.Executable executable)
   {
      return new ExecutableAdapter(executable);
   }

   static TypeAdapter adapt(Ap.Type type)
   {
      return new TypeAdapter(type);
   }

   static GenericAdapter adapt(Ap.Generic generic)
   {
      return new GenericAdapter(generic);
   }

   static ModuleAdapter adapt(Ap.Module module)
   {
      return new ModuleAdapter(module);
   }

   static NullAdapter adapt(Ap.Null aNull)
   {
      return new NullAdapter(aNull);
   }

   static PackageAdapter adapt(Ap.Package aPackage)
   {
      return new PackageAdapter(aPackage);
   }

   static PrimitiveAdapter adapt(Ap.Primitive primitive)
   {
      return new PrimitiveAdapter(primitive);
   }

   static VoidAdapter adapt(Ap.Void aVoid)
   {
      return new VoidAdapter(aVoid);
   }

   static WildcardAdapter adapt(Ap.Wildcard wildcard)
   {
      return new WildcardAdapter(wildcard);
   }

   static AnnotationableAdapter adapt(Ap.Annotationable annotationable)
   {
      return new AnnotationableAdapter(annotationable);
   }

   static RecordComponentAdapter adapt(Ap.RecordComponent recordComponent)
   {
      return new RecordComponentAdapter(recordComponent);
   }

   static VariableAdapter adapt(Ap.Variable variable)
   {
      return new VariableAdapter(variable);
   }

   static ExportsAdapter adapt(Ap.Exports exports)
   {
      return new ExportsAdapter(exports);
   }

   static OpensAdapter adapt(Ap.Opens opens)
   {
      return new OpensAdapter(opens);
   }

   static RequiresAdapter adapt(Ap.Requires requires)
   {
      return new RequiresAdapter(requires);
   }

   static UsesAdapter adapt(Ap.Uses uses)
   {
      return new UsesAdapter(uses);
   }

   //jdk -> shadow

   static Ap.Executable adapt(Ap.Context context, ExecutableElement element)
   {
      return switch (element.getKind())
      {
         case METHOD -> new MethodImpl(context, element);
         case CONSTRUCTOR -> new ConstructorImpl(context, element);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.ExecutableElement that is nether a METHOD, FIELD or CONSTRUCTOR\n" + element);
      };
   }

   static Ap.Variable adapt(Ap.Context context, VariableElement element)
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

   static Ap.RecordComponent adapt(Ap.Context context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   static Ap.Module adapt(Ap.Context context, ModuleElement moduleElement)
   {
      return new ModuleImpl(context, moduleElement);
   }

   static Ap.Wildcard adapt(Ap.Context context, WildcardType wildcardType)
   {
      return new WildcardImpl(context, wildcardType);
   }

   static Ap.Declared adapt(Ap.Context context, TypeElement typeElement)
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

   static Ap.Generic adapt(Ap.Context context, TypeParameterElement typeParameterElement)
   {
      return new GenericImpl(context, typeParameterElement);
   }

   /// This Method constructs [Object]s based on [Element]s. They represent a specific usage. `List<String>` for example.
   /// If you want to represent the abstract code like `List<T>` then use [TypeMirror]s.
   static Object adapt(Ap.Context context, Element element)
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

   /// This Method constructs [Ap.Type]s based on [TypeMirror]s. They represent the abstract code. `List<T>` for example.
   /// If you want a specific usage like `List<String>` then you should use the specific [Element].
   ///
   /// [TypeKind#EXECUTABLE], [TypeKind#NONE], [TypeKind#PACKAGE], [TypeKind#MODULE] can not be used with this method as information would be
   /// missing, that is needed for there creation. Use [Element]s to create them.
   static Ap.Type adapt(Ap.Context context, TypeMirror typeMirror)
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
               " use Ap.Adapters.adapt(LM_Context, javax.lang.model.element.Element) as they don't extend " + Ap.Type.class.getSimpleName());
         case ERROR -> throw new IllegalArgumentException();
         case OTHER, UNION ->
               throw new IllegalArgumentException(typeMirror.getKind() + " is not exposed through annotation processing and not supported");
      };
   }

   static Ap.Declared adapt(Ap.Context context, DeclaredType declaredType)
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

   /// used to create [Ap.Void].
   /// For [TypeKind#PACKAGE] use [#adapt(Ap.Context , PackageElement)] and for [TypeKind#MODULE] use [#adapt(Ap.Context , ModuleElement)].
   /// javax.lang.model has no specific type that represents [Void], this api can therefore not be more specific.
   static Ap.Void adapt(Ap.Context context, NoType noType)
   {
      if (!TypeKind.VOID.equals(noType.getKind()))
      {
         throw new IllegalArgumentException(
               "For javax.lang.model.type.NoType other then Void use LM_Adapters.adapt(LM_Context, Element/ModuleElement/PackageElement)");
      }
      return new VoidImpl(context, noType);
   }

   static Ap.Generic adapt(Ap.Context context, IntersectionType intersectionType)
   {
      return new IntersectionImpl(context, intersectionType);
   }

   static Ap.Generic adapt(Ap.Context context, TypeVariable typeVariable)
   {
      return new GenericImpl(context, typeVariable);
   }

   static Ap.Null adapt(Ap.Context context, NullType nullType)
   {
      return new NullImpl(context, nullType);
   }

   static Ap.Array adapt(Ap.Context context, ArrayType arrayType)
   {
      return new ArrayImpl(context, arrayType);
   }

   static Ap.Primitive adapt(Ap.Context context, PrimitiveType primitiveType)
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

   static Ap.Package adapt(Ap.Context context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static Ap.AnnotationUsage adapt(Ap.Context context, AnnotationMirror annotationMirror)
   {
      return adapt(context, singletonList(annotationMirror)).get(0);
   }

   static List<Ap.AnnotationUsage> adapt(Ap.Context context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static Ap.Exports adapt(Ap.Context context, ModuleElement.ExportsDirective exportsDirective)
   {
      return new ExportsImpl(context, exportsDirective);
   }

   static Ap.Opens adapt(Ap.Context context, ModuleElement.OpensDirective opensDirective)
   {
      return new OpensImpl(context, opensDirective);
   }

   static Ap.Provides adapt(Ap.Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      return new ProvidesImpl(context, providesDirective);
   }

   static Ap.Requires adapt(Ap.Context context, ModuleElement.RequiresDirective requiresDirective)
   {
      return new RequiresImpl(context, requiresDirective);
   }

   static Ap.Uses adapt(Ap.Context context, ModuleElement.UsesDirective usesDirective)
   {
      return new UsesImpl(context, usesDirective);
   }

   static AnnotationValue adapt(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static ContextAdapter adapt(Ap.Context context)
   {
      return new ContextAdapter(context);
   }
}
