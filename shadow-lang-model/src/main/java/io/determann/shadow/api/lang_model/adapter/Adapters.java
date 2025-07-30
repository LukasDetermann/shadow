package io.determann.shadow.api.lang_model.adapter;

import io.determann.shadow.api.lang_model.LM;
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

public interface Adapters
{
   //shadow -> jdk

   static AnnotationUsageAdapter adapt(LM.AnnotationUsage annotationUsage)
   {
      return new AnnotationUsageAdapter(annotationUsage);
   }

   static DeclaredAdapter adapt(LM.Declared declared)
   {
      return new DeclaredAdapter(declared);
   }

   static ArrayAdapter adapt(LM.Array array)
   {
      return new ArrayAdapter(array);
   }

   static ExecutableAdapter adapt(LM.Executable executable)
   {
      return new ExecutableAdapter(executable);
   }

   static TypeAdapter adapt(LM.Type type)
   {
      return new TypeAdapter(type);
   }

   static GenericAdapter adapt(LM.Generic generic)
   {
      return new GenericAdapter(generic);
   }

   static ModuleAdapter adapt(LM.Module module)
   {
      return new ModuleAdapter(module);
   }

   static NullAdapter adapt(LM.Null aNull)
   {
      return new NullAdapter(aNull);
   }

   static PackageAdapter adapt(LM.Package aPackage)
   {
      return new PackageAdapter(aPackage);
   }

   static PrimitiveAdapter adapt(LM.Primitive primitive)
   {
      return new PrimitiveAdapter(primitive);
   }

   static VoidAdapter adapt(LM.Void aVoid)
   {
      return new VoidAdapter(aVoid);
   }

   static WildcardAdapter adapt(LM.Wildcard wildcard)
   {
      return new WildcardAdapter(wildcard);
   }

   static AnnotationableAdapter adapt(LM.Annotationable annotationable)
   {
      return new AnnotationableAdapter(annotationable);
   }

   static RecordComponentAdapter adapt(LM.RecordComponent recordComponent)
   {
      return new RecordComponentAdapter(recordComponent);
   }

   static VariableAdapter adapt(LM.Variable variable)
   {
      return new VariableAdapter(variable);
   }

   static ExportsAdapter adapt(LM.Exports exports)
   {
      return new ExportsAdapter(exports);
   }

   static OpensAdapter adapt(LM.Opens opens)
   {
      return new OpensAdapter(opens);
   }

   static RequiresAdapter adapt(LM.Requires requires)
   {
      return new RequiresAdapter(requires);
   }

   static UsesAdapter adapt(LM.Uses uses)
   {
      return new UsesAdapter(uses);
   }

   //jdk -> shadow

   static LM.Context adapt(Types types, Elements elements)
   {
      return new LangModelContextImpl(types, elements);
   }

   static LM.Executable adapt(LM.Context context, ExecutableElement element)
   {
      return switch (element.getKind())
      {
         case METHOD -> new MethodImpl(context, element);
         case CONSTRUCTOR -> new ConstructorImpl(context, element);
         default -> throw new IllegalStateException(
               "javax.lang.model.element.ExecutableElement that is nether a METHOD, FIELD or CONSTRUCTOR\n" + element);
      };
   }

   static LM.Variable adapt(LM.Context context, VariableElement element)
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

   static LM.RecordComponent adapt(LM.Context context, RecordComponentElement recordComponentElement)
   {
      return new RecordComponentImpl(context, recordComponentElement);
   }

   static LM.Module adapt(LM.Context context, ModuleElement moduleElement)
   {
      return new ModuleImpl(context, moduleElement);
   }

   static LM.Wildcard adapt(LM.Context context, WildcardType wildcardType)
   {
      return new WildcardImpl(context, wildcardType);
   }

   static LM.Declared adapt(LM.Context context, TypeElement typeElement)
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

   static LM.Generic adapt(LM.Context context, TypeParameterElement typeParameterElement)
   {
      return new GenericImpl(context, typeParameterElement);
   }

   /// This Method constructs [Object]s based on [Element]s. They represent a specific usage. `List<String>` for example.
   /// If you want to represent the abstract code like `List<T>` then use [TypeMirror]s.
   static Object adapt(LM.Context context, Element element)
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

   /// This Method constructs [LM.Type]s based on [TypeMirror]s. They represent the abstract code. `List<T>` for example.
   /// If you want a specific usage like `List<String>` then you should use the specific [Element].
   ///
   /// [TypeKind#EXECUTABLE], [TypeKind#NONE], [TypeKind#PACKAGE], [TypeKind#MODULE] can not be used with this method as information would be
   /// missing, that is needed for there creation. Use [Element]s to create them.
   static LM.Type adapt(LM.Context context, TypeMirror typeMirror)
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
               " use LM_Adapters.adapt(LM_Context, javax.lang.model.element.Element) as they don't extend " + LM.Type.class.getSimpleName());
         case ERROR -> throw new IllegalArgumentException();
         case OTHER, UNION ->
               throw new IllegalArgumentException(typeMirror.getKind() + " is not exposed through annotation processing and not supported");
      };
   }

   static LM.Declared adapt(LM.Context context, DeclaredType declaredType)
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

   /// used to create [LM.Void].
   /// For [TypeKind#PACKAGE] use [#adapt(LM.Context , PackageElement)] and for [TypeKind#MODULE] use [#adapt(LM.Context , ModuleElement)].
   /// javax.lang.model has no specific type that represents [Void], this api can therefore not be more specific.
   static LM.Void adapt(LM.Context context, NoType noType)
   {
      if (!TypeKind.VOID.equals(noType.getKind()))
      {
         throw new IllegalArgumentException(
               "For javax.lang.model.type.NoType other then Void use LM_Adapters.adapt(LM_Context, Element/ModuleElement/PackageElement)");
      }
      return new VoidImpl(context, noType);
   }

   static LM.Generic adapt(LM.Context context, IntersectionType intersectionType)
   {
      return new IntersectionImpl(context, intersectionType);
   }

   static LM.Generic adapt(LM.Context context, TypeVariable typeVariable)
   {
      return new GenericImpl(context, typeVariable);
   }

   static LM.Null adapt(LM.Context context, NullType nullType)
   {
      return new NullImpl(context, nullType);
   }

   static LM.Array adapt(LM.Context context, ArrayType arrayType)
   {
      return new ArrayImpl(context, arrayType);
   }

   static LM.Primitive adapt(LM.Context context, PrimitiveType primitiveType)
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

   static LM.Package adapt(LM.Context context, PackageElement packageElement)
   {
      return new PackageImpl(context, packageElement);
   }

   static LM.AnnotationUsage adapt(LM.Context context, AnnotationMirror annotationMirror)
   {
      return adapt(context, singletonList(annotationMirror)).get(0);
   }

   static List<LM.AnnotationUsage> adapt(LM.Context context, List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(context, annotationMirrors);
   }

   static LM.Exports adapt(LM.Context context, ModuleElement.ExportsDirective exportsDirective)
   {
      return new ExportsImpl(context, exportsDirective);
   }

   static LM.Opens adapt(LM.Context context, ModuleElement.OpensDirective opensDirective)
   {
      return new OpensImpl(context, opensDirective);
   }

   static LM.Provides adapt(LM.Context context, ModuleElement.ProvidesDirective providesDirective)
   {
      return new ProvidesImpl(context, providesDirective);
   }

   static LM.Requires adapt(LM.Context context, ModuleElement.RequiresDirective requiresDirective)
   {
      return new RequiresImpl(context, requiresDirective);
   }

   static LM.Uses adapt(LM.Context context, ModuleElement.UsesDirective usesDirective)
   {
      return new UsesImpl(context, usesDirective);
   }

   static AnnotationValue adapt(AnnotationValue annotationValue)
   {
      return ((AnnotationValueImpl) annotationValue).getAnnotationValue();
   }

   static ContextAdapter adapt(LM.Context context)
   {
      return new ContextAdapter(context);
   }
}
