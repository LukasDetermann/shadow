package io.determann.shadow.impl;

import io.determann.shadow.api.ShadowApi;
import io.determann.shadow.api.ShadowFactory;
import io.determann.shadow.api.shadow.AnnotationUsage;
import io.determann.shadow.api.shadow.Shadow;
import io.determann.shadow.impl.annotationvalue.AnnotationUsageImpl;
import io.determann.shadow.impl.shadow.*;

import javax.lang.model.element.*;
import javax.lang.model.type.*;
import java.util.List;

class ShadowFactoryImpl implements ShadowFactory
{
   private final ShadowApi shadowApi;

   ShadowFactoryImpl(ShadowApi shadowApi)
   {
      this.shadowApi = shadowApi;
   }

   @Override
   public <SHADOW extends Shadow> SHADOW shadowFromElement(Element element)
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

   @Override
   public <SHADOW extends Shadow> SHADOW shadowFromType(TypeMirror typeMirror)
   {
      //noinspection unchecked
      return (SHADOW) switch (typeMirror.getKind())
      {
         case BOOLEAN, BYTE, SHORT, INT, LONG, CHAR, FLOAT, DOUBLE -> new PrimitiveImpl(shadowApi, (PrimitiveType) typeMirror);
         case ARRAY -> new ArrayImpl(shadowApi, (ArrayType) typeMirror);
         case DECLARED -> switch (shadowApi.getJdkApiContext().getProcessingEnv().getTypeUtils().asElement(typeMirror).getKind())
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

   @Override
   public List<AnnotationUsage> annotationUsages(List<? extends AnnotationMirror> annotationMirrors)
   {
      return AnnotationUsageImpl.from(shadowApi, annotationMirrors);
   }
}
